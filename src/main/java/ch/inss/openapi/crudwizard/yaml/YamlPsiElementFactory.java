package ch.inss.openapi.crudwizard.yaml;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLFileType;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class YamlPsiElementFactory {


    @NotNull
    public static PsiFile createDummyFile(@NotNull Project project, @NotNull String filename, @NotNull String content) {
        return PsiFileFactory.getInstance(project).createFileFromText(filename, YAMLFileType.YML, content, System.currentTimeMillis(), false);
    }
}