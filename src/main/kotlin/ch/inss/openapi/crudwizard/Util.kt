package ch.inss.openapi.crudwizard

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

object Util {

    private const val GROUP_DISPLAY_ID = "ch.inss.openapi.crudwizard.action.PopupDialogAction"

    fun notifyMessage(project: Project?, message: String, title: String?) {
        Notifications.Bus.notify(
                Notification(GROUP_DISPLAY_ID, title!!, "<html>$message</html>", NotificationType.INFORMATION),
                project
        )
    }

    fun showWarning(message: String, title: String) {
        Messages.showWarningDialog(message, title)
    }

    fun showError(message: String) {
        Messages.showErrorDialog(message, "Error in OpenAPI CRUD Wizard")
    }
}