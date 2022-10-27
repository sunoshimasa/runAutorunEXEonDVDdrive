/*
 * DVD メディアで配布され、Autorun.exe で起動するソフトウェア媒体をプログラムから起動する
 * DVD メディアの Autorun.exe を実行して起動するソフトウェアの中には、起動する際に
 * カレントドライブが DVD メディアと異なると Autorun.exe が起動できない場合がある
 * 例えば、カレントドライブが C: のとき、また、DVD ドライブが D: のときカレントドライブの C:から
 * D:\Autorun.exe を実行すると起動エラーとなるソフトウェアが存在する
 */
package runAutorunEXEonDVDdrive;

import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileSystemView;

public class runAutorunEXEonDVDdrive {

	public static void main(String[] args) {

		FileSystemView fsv = FileSystemView.getFileSystemView();
		// getSystemTypeDescription が返すString値は"CD ドライブ"
		String drv_type_string_value = "CD ドライブ";
		// PCローカルな全ドライブをリストアップする
		File[] drives = File.listRoots();
		//
		// ローカルドライブから DVD ドライブを探索してドライブレターを取得する
		//
		if(drives != null && drives.length > 0) {
			for (File drv : drives) {
				if (fsv.getSystemTypeDescription(drv).equals(drv_type_string_value)) {
					// DVD ドライブが見つかったら ProcessBuilder で cmdシェルを起動する
					// 起動したcmdシェルで実行するコマンドはDVDドライブのルートにあるAutorun.exe
					ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "Autorun.exe");
					// DVDドライブのドライブ名を取得する
					File dir = new File(drv.toString());
					// ProcessBuilderで起動したcmdシェルのカレントドライブをDVDドライブに切り替える
					pb.directory(dir);
					// DVDドライブのルートにあるAutorun.exeをProcessBuilderのstart()で実行する
					try {
						pb.start();
					} catch (IOException e) {
						// Autorun.exeが実行できない
						System.out.println("DVDドライブちゃんと接続してますか？");
					}
				}
			}
		}
	}
}
