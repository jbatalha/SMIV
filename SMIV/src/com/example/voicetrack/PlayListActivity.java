package com.example.voicetrack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class PlayListActivity extends ListActivity implements Serializable {

	ListView list;
	StrongerAdapter adapter;
	EditText inputText = null;

	public static final String KEY_POSITON = "position";
	public static final String LIST_CURRENT = "ListCurrent";

	// lista de musica
	public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);

		ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();
		SongsManager sm = new SongsManager(getApplicationContext());
		// pega todas as musicas do SD
		this.songsList = sm.getPlayList();
		// looping through playlist
		for (int i = 0; i < songsList.size(); i++) {
			// criando um novo HashMap
			HashMap<String, String> song = songsList.get(i);
			// adicionando HashList to ArrayList
			songsListData.add(song);
		}
		list = (ListView) findViewById(android.R.id.list);

		// jogando  no adapter o que foi dado por ArrayList
		adapter = new StrongerAdapter(this, songsList);

		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting listitem index

				Intent intent = new Intent(getApplicationContext(),
						AndroidBuildingMusicPlayerActivity.class);

				intent.putExtra(KEY_POSITON, view.getId());
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);

			}

		});

		inputText = (EditText) findViewById(R.id.editText1);
		
		/**
		 *  Adicionando um Listener (age conforme o estado atual da EditText)
		 */
		inputText.addTextChangedListener(new TextWatcher() {
			private Timer timer = new Timer();
			private final long delayInMillis = 3000;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				PlayListActivity.this.adapter.getFilter().filter(s.toString());

				timer.cancel();
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						PlayListActivity.this.list.getOnItemClickListener()
								.onItemClick(
										PlayListActivity.this.list,
										PlayListActivity.this.list
												.getChildAt(0), 0, -1);
					}

				}, delayInMillis);

			}
		});
	}
}
