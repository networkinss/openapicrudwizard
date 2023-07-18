package ch.inss.openapi.crudwizard.action

//import ch.inss.openapi.crudwizard.CheckLicense
import ch.inss.openapi.crudwizard.CheckLicense
import ch.inss.openapi.crudwizard.OutputWraper
import ch.inss.openapi.crudwizard.Util
import ch.inss.openapi.crudwizard.yaml.YamlPsiElementFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.util.IncorrectOperationException
import javax.swing.Icon

/**
 * Action class to demonstrate how to interact with the IntelliJ Platform.
 * The only action this class performs is to provide the user with a popup dialog as feedback.
 * Typically, this class is instantiated by the IntelliJ Platform framework based on declarations
 * in the plugin.xml file. But when added at runtime this class is instantiated by an action group.
 */
class PopupDialogAction : AnAction {
    /**
     * This default constructor is used by the IntelliJ Platform framework to instantiate this class based on plugin.xml
     * declarations. Only needed in [PopupDialogAction] class because a second constructor is overridden.
     *
     */
    constructor() : super() {}

    /**
     * This constructor is used to support dynamically added menu actions.
     * It sets the text, description to be displayed for the menu item.
     * Otherwise, the default AnAction constructor is used by the IntelliJ Platform.
     *
     * @param text        The text to be displayed as a menu item.
     * @param description The description of the menu item.
     * @param icon        The icon to be used with the menu item.
     */
    constructor(text: String?, description: String?, icon: Icon?) : super(text, description, icon) {}

    private val messageTitle = "OpenAPI CRUD Wizard Pro"

    /**
     * Create a new OpenAPI document.
     */
    override fun actionPerformed(e: AnActionEvent) {
        var inputText = ""
        var fileName = "openapi"
        val fileEnding = ".yaml"
        val project = e.getRequiredData(CommonDataKeys.PROJECT)
        /* Select file element */
        val element = e.getData(CommonDataKeys.NAVIGATABLE)
        val psiFile: PsiFile?
        /* Select text within file editor */
        val editor: Editor
        val primaryCaret: Caret
        /* Message */
        var dlgMsg = "An internal error occurred."
        var dlgTitle = messageTitle
        /*License issues */
        val isLicensed = CheckLicense.isLicensed()

        var isFromText = false
        /* Case a file is selected. */
        if (element is PsiElement) {
            psiFile = (element as PsiElement).containingFile ?: return
            val fileType = psiFile.fileType
            if (fileType.name == "YAML" || fileType.name.lowercase() == "plain_text") {
                inputText = psiFile.text
                isFromText = true
            } else if (fileType.name.lowercase() == "native") {
                if (isLicensed) {
                    isFromText = false
                } else {
                    Util.showError("You need a license to process Excel files.")
                    CheckLicense.requestLicense("You need a license to generate OpenAPI documents.")
                    return
                }
            } else {
                Util.showError("Only the Yaml filetype is supported as input.")
                return
            }

        } else /* Case a text within a file editor is selected. */ {
            // Editor and Project were verified in update(), so they are not null.
            try {
                editor = e.getRequiredData(CommonDataKeys.EDITOR)
                psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE)
                primaryCaret = editor.caretModel.primaryCaret
                if (primaryCaret.selectedText != null) {
                    inputText = primaryCaret.selectedText.toString()
                    primaryCaret.removeSelection()
                    isFromText = true
                }
            } catch (ex: AssertionError) {
                println("Could not read selected content. $ex")
                Util.showError("Could not read selected content. $ex")
                return
            } catch (ex: Exception) {
                println("Could not read selected content. $ex")
                Util.showError("Could not read selected content. $ex")
                return
            }
        }
        /* Validate */
        val output: OutputWraper
        if (isFromText) {
            if (inputText.length < 3) {
                Util.showError("Please select a Yaml formatted text.")
                return
            }
            /* Actual transformation from String to OAD3 Yaml String.
            **/
            output = fromString(inputText, isLicensed)
        } else {
            val vf = psiFile.virtualFile
            vf.inputStream
            output = Jo.joExcelToOas3String(vf.inputStream)
        }
        val outputText = output.outputText
        var isCreated = false
        /* Create new file with content from inputText. */
        WriteCommandAction.runWriteCommandAction(
                project
        ) {
            psiFile.let {
                var noNameFound = true
                var count = 0
                var postfix = ""
                while (noNameFound) {
                    try {
                        psiFile.containingDirectory.checkCreateFile("$fileName$postfix$fileEnding")
                        fileName = "$fileName$postfix$fileEnding"
                        break
                    } catch (ex: IncorrectOperationException) {
                    }
                    if (count > 9) {
                        dlgMsg =
                                "Did not find a free filename.\n Files with name \"openapi.yaml\" (extended by 1-10) already exist."
                        dlgTitle = messageTitle
                        fileName = ""
                        noNameFound = false
                        Util.showError(dlgMsg)
                    }
                    count++
                    postfix = "_$count"
                }
                if (fileName.isNotEmpty()) {
                    val file: PsiFile = YamlPsiElementFactory.createDummyFile(project, fileName, outputText)
                    psiFile.containingDirectory.add(file)
                    dlgMsg = "Written openapi file to: <strong>'" + file.name + "'</strong>."
                    isCreated = true
                }
            }
        }
        if (isCreated) Util.notifyMessage(project, dlgMsg, dlgTitle)
    }

    private fun fromString(inputText: String, isPro: Boolean): OutputWraper {
        val output = Jo.joStringToOas3(inputText, if (isPro) 100 else 3)
        val msg: String = output.error
        if (msg.isNotEmpty() && !msg.endsWith("null")) {
            Util.showError(msg)
        } else if (output.warning.isNotEmpty()) {
            Util.showWarning(output.warning, messageTitle)
        }
        return output
    }


    /**
     * Determines whether this menu item is available for the current context.
     * Requires a project to be open.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability based on whether a project is open
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
}