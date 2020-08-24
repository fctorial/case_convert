import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.ui.MessageType
import com.intellij.psi.PsiDocumentManager
import com.intellij.refactoring.RefactoringFactory

val ng = NotificationGroup("Case Convertor", NotificationDisplayType.BALLOON)
fun notify(msg: Any) {
    Notifications.Bus.notify(ng.createNotification(msg.toString(), MessageType.INFO))
}

abstract class BaseAction : AnAction() {
    abstract fun processParts(ws: List<String>): String

    override fun actionPerformed(ev: AnActionEvent) {
        val status = _actionPerformed(ev)
        notify(status)
    }

    fun _actionPerformed(ev: AnActionEvent) : String {
        val project = ev.project ?: return "no project"
        val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return "no editor"
        val doc = editor.document
        val caret = editor.caretModel.primaryCaret
        val psi = PsiDocumentManager.getInstance(project).getPsiFile(doc) ?: return "no psifile?"

        val elem = psi.findElementAt(caret.offset) ?: return "put the caret on an identifier"
        val orig = elem.node.text
        val parts = parseWord(orig)
        val new = processParts(parts)
        if (! elem.isWritable) return "read only reference"
        RefactoringFactory.getInstance(project).createRename(elem, processParts(parts)).run()
        return "renamed $orig to $new"
    }

}

class SnakeCase : BaseAction() {
    override fun processParts(ws: List<String>): String {
        var res = ws[0].toLowerCase()
        ws.subList(1, ws.size).forEach {
            res = res + "_" + it.toLowerCase()
        }
        return res
    }
}

class CababCase : BaseAction() {
    override fun processParts(ws: List<String>): String {
        var res = ws[0].toLowerCase()
        ws.subList(1, ws.size).forEach {
            res = res + "-" + it.toLowerCase()
        }
        return res
    }
}

class PascalCase : BaseAction() {
    override fun processParts(ws: List<String>): String {
        var res = ""
        ws.forEach {
            res += it.capitalize()
        }
        return res
    }
}

class CamelCase : BaseAction() {
    override fun processParts(ws: List<String>): String {
        var res = ws[0].toLowerCase()
        ws.subList(1, ws.size).forEach {
            res += it.capitalize()
        }
        return res
    }
}