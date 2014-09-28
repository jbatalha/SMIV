package com.example.voicetrack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

//classe que pega as musicas no cartão
public class SongsManager implements Serializable {

	public static final String KEY_SONG = "song"; // parent node
	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_ARTIST = "artist";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_ALBUM_ART = "album_art";
	public static final String KEY_ALBUM = "album";
	public static final String KEY_POSITON = "position";
	public static final String KEY_POSITON_LISTMANAGER = "position_in_listmanager";
	private Context context;
	String[] colunas = {

	MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION };
	String[] projecao = { MediaStore.Audio.Albums.ALBUM_ART };

	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	public SongsManager(Context context) {
		this.context = context;
	}

	public ArrayList<HashMap<String, String>> getPlayList() {

		Cursor cursor = this.context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, colunas, null,
				null, null);
		
		// pegando o index das colunas :D
		int indexColuna = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
		int artistColuna = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
		int durationColuna = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
		int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
		
		// percorrendo a tabela
		if (cursor.moveToFirst()) {
			int position = 0;
			while (cursor.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("songPath", cursor.getString(indexColuna));
				map.put("songTitle", cursor.getString(titleColumn));
				map.put(KEY_POSITON, String.valueOf(position));
				map.put(KEY_ID, cursor.getString(indexColuna));
				map.put(KEY_TITLE, cursor.getString(titleColumn));
				map.put(KEY_ARTIST, cursor.getString(artistColuna));
				map.put(KEY_DURATION,Utils.parseToTime(cursor.getLong(durationColuna)));
				map.put(KEY_POSITON_LISTMANAGER, String.valueOf(position));
				
		        

				position++;

				// adicionando HashList no ArrayList
				songsList.add(map);

			}
		}
		return songsList;

	}
}
