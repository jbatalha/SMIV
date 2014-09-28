package com.example.voicetrack;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class StrongerAdapter extends BaseAdapter implements Filterable,
		Serializable {
	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private ArrayList<HashMap<String, String>> dataHistory;
	private static LayoutInflater inflater = null;
	final int stub_id = R.drawable.no_image;
	ImageLoader imageLoader;
	HashMap<String, String> song = new HashMap<String, String>();

	public StrongerAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		dataHistory = getDataHistory(data);

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());

	}

	// cria o Data Auxiliar
	private ArrayList<HashMap<String, String>> getDataHistory(
			ArrayList<HashMap<String, String>> data) {
		ArrayList<HashMap<String, String>> dataHistory = new ArrayList<HashMap<String, String>>();

		for (HashMap<String, String> hashMap : data) {
			dataHistory.add(hashMap);
		}
		return dataHistory;
	}

	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_row, null);

		TextView title = (TextView) vi.findViewById(R.id.title); // titlo
		TextView artist = (TextView) vi.findViewById(R.id.artist); // artista

		TextView duration = (TextView) vi.findViewById(R.id.duration); // duração
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // imagem

		song = data.get(position);

		// Seta o id, baseado na posicao onde ele estiver na lista de todas as
		// musicas

		vi.setId(Integer.parseInt(song
				.get(SongsManager.KEY_POSITON_LISTMANAGER)));

		// Definimos todos os valores em listview
		title.setText(song.get(SongsManager.KEY_TITLE));
		artist.setText(song.get(SongsManager.KEY_ARTIST));
		duration.setText(song.get(SongsManager.KEY_DURATION));
		imageLoader.DisplayImage(song.get(SongsManager.KEY_ALBUM_ART),
				thumb_image);
		// trabalha com a imagem
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(song.get(SongsManager.KEY_ID));
		byte[] artBytes = mmr.getEmbeddedPicture();
		if (artBytes != null) {
			InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
			Bitmap bm = BitmapFactory.decodeStream(is);
			thumb_image.setImageBitmap(bm);
		}

		return vi;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				if (results.count == 0) {
					data = getDataHistory(dataHistory);
					notifyDataSetInvalidated();
				} else {
					data = (ArrayList<HashMap<String, String>>) results.values;
					StrongerAdapter.this.notifyDataSetChanged();
				}

			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();

				if (constraint == null || constraint.length() == 0) {
					// Nenhum filtro implementado voltamos toda a lista
					results.values = getDataHistory(dataHistory);
					results.count = dataHistory.size();

				} else { // Realizamos operação de filtragem
					ArrayList<HashMap<String, String>> filteredRowItems = 
										new ArrayList<HashMap<String, String>>();

					for (HashMap<String, String> p : data) {
						if (p.get(SongsManager.KEY_TITLE).toUpperCase()
								.contains(constraint.toString().toUpperCase())) {
							filteredRowItems.add(p);
						}
					}
					results.values = filteredRowItems;
					results.count = filteredRowItems.size();
				}
				return results;
			}
		};
	}

	public class ImageLoader {
		// classe que trabalha caso não se tenha a imagem do album
		public ImageLoader(Context context) {

		}

		public void DisplayImage(String coverPath, ImageView imageView) {

			Drawable img = Drawable.createFromPath(coverPath);
			imageView.setImageDrawable(img);
			if (img == null) {

				imageView.setImageResource(stub_id);

			}
		}
	}
}
