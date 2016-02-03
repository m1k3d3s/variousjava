
public class NoteSystemMain {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				NoteSystemUI nsui = new NoteSystemUI();
				nsui.buildAndShow();
			}
		});
	}
}
