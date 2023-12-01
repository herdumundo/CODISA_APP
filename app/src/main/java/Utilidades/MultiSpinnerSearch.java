package Utilidades;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.example.codisa_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

public class MultiSpinnerSearch extends AppCompatSpinner implements OnCancelListener {

	public static AlertDialog.Builder builder;
	public static AlertDialog ad;
	private boolean highlightSelected = false;
//	private int highlightColor = ContextCompat.getColor(getContext(), R.color.list_selected);
	private int textColor = Color.BLACK;
	private int selected = 0;
	private String defaultText = "";
	private String spinnerTitle = "";
	private String emptyTitle = "Not Found!";
	//private String spinnerTitle = "Not Found!";
	private String searchHint = "BUSQUEDA";
	private String clearText = "Clear All";
	private boolean colorSeparation = false;
	private boolean isShowSelectAllButton = true;
	private MultiSpinnerListener listener;
	private MyAdapter adapter;
	private List<ArrayListContenedor> items;
	private boolean isSearchEnabled = true;

	public MultiSpinnerSearch(Context context) {
		super(context);
	}

	public MultiSpinnerSearch(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		TypedArray a = arg0.obtainStyledAttributes(arg1, R.styleable.MultiSpinnerSearch);
		for (int i = 0; i < a.getIndexCount(); ++i) {
			int attr = a.getIndex(i);
			if (attr == R.styleable.MultiSpinnerSearch_hintText) {
				this.setHintText(a.getString(attr));
				spinnerTitle = this.getHintText();
				defaultText = spinnerTitle;
				break;
			} else if (attr == R.styleable.MultiSpinnerSearch_highlightSelected) {
				highlightSelected = a.getBoolean(attr, false);
			} else if (attr == R.styleable.MultiSpinnerSearch_highlightColor) {
			//	highlightColor = a.getColor(attr, ContextCompat.getColor(getContext(), R.color.naranja));
			} else if (attr == R.styleable.MultiSpinnerSearch_textColor) {
				textColor = a.getColor(attr, Color.BLACK);
			}else if (attr == R.styleable.MultiSpinnerSearch_clearText){
				this.setClearText(a.getString(attr));
			}
		}

		//Log.i(TAG, "spinnerTitle: " + spinnerTitle);
		a.recycle();
	}

	public MultiSpinnerSearch(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	public void setColorSeparation(boolean colorSeparation) {
		this.colorSeparation = colorSeparation;
	}

	public String getHintText() {
		return this.spinnerTitle;
	}

	public void setHintText(String hintText) {
		this.spinnerTitle = hintText;
		defaultText = spinnerTitle;
	}

	public void setClearText(String clearText){
		this.clearText = clearText;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// refresh text on spinner

		StringBuilder spinnerBuffer = new StringBuilder();

		ArrayList<ArrayListContenedor> selectedData = new ArrayList<>();
		for (int i = 0; i < items.size(); i++) {
			ArrayListContenedor currentData = items.get(i);
			if (currentData.isSelected()) {
				selectedData.add(currentData);
				spinnerBuffer.append(currentData.getName());
				spinnerBuffer.append(", ");
			}
		}

		String spinnerText = spinnerBuffer.toString();
		if (spinnerText.length() > 2)
			spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
		else
			spinnerText = this.getHintText();

		ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{spinnerText});
		setAdapter(adapterSpinner);

		if (adapter != null)
			adapter.notifyDataSetChanged();

		listener.onItemsSelected(selectedData);


	/*	new Thread(() -> {
			Instrumentation inst = new Instrumentation();
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
		}).start(); */
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public boolean performClick() {

		super.performClick();
		builder = new AlertDialog.Builder(getContext());
		//builder.setTitle(spinnerTitle);

		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View view = inflater.inflate(R.layout.alert_dialog_listview_search, null);
		builder.setView(view);

		final ListView listView = view.findViewById(R.id.alertSearchListView);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setFastScrollEnabled(true);
		adapter = new MyAdapter(getContext(), items);
		listView.setAdapter(adapter);
		final TextView emptyText = view.findViewById(R.id.empty);
		final TextView spinerTitle = view.findViewById(R.id.spinerTitle);
		emptyText.setText(emptyTitle);
		spinerTitle.setText(spinnerTitle);
		listView.setEmptyView(emptyText);

		final EditText editText = view.findViewById(R.id.alertSearchEditText);
		if (isSearchEnabled) {
			editText.setVisibility(VISIBLE);
			editText.setHint(searchHint);
			editText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					adapter.getFilter().filter(s.toString());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});
		} else {
			editText.setVisibility(GONE);
		}

		/*
		 * For selected items
		 */
		selected = 0;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).isSelected())
				selected++;
		}

        /*
        Added Select all Dialog Button.
         */
		if (isShowSelectAllButton// && limit == -1
		) {
			builder.setNeutralButton(R.string.todos, (dialog, which) -> {
				for (int i = 0; i < adapter.arrayList.size(); i++) {
					adapter.arrayList.get(i).setSelected(true);
				}
				adapter.notifyDataSetChanged();
				// To call onCancel listner and set title of selected items.
				dialog.cancel();
			});
		}

		builder.setPositiveButton(R.string.Aceptar, (dialog, which) -> {
			 dialog.cancel();
		});


		builder.setNegativeButton(R.string.ninguno, (dialog, which) -> {
			//Log.i(TAG, " ITEMS : " + items.size());
			for (int i = 0; i < adapter.arrayList.size(); i++) {
				adapter.arrayList.get(i).setSelected(false);
			}
			adapter.notifyDataSetChanged();
			dialog.cancel();
		});

		builder.setOnCancelListener(this);
		ad = builder.show();
		ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.azul_claro));
		ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.azul_claro));
		ad.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.azul_claro));
		ad.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
		ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
		ad.getButton(AlertDialog.BUTTON_NEUTRAL).setAllCaps(false);
		ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
		ad.getButton(AlertDialog.BUTTON_NEUTRAL).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
		ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);


		Objects.requireNonNull(ad.getWindow()).setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


		return true;
	}

	public void setItems(List<ArrayListContenedor> items, MultiSpinnerListener listener) {

		this.items = items;
		this.listener = listener;

		StringBuilder spinnerBuffer = new StringBuilder();
		int contador=0;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).isSelected()) {
				spinnerBuffer.append(items.get(i).getName());
				spinnerBuffer.append(", ");
				contador++;
			}

		}

		if (spinnerBuffer.length() > 2)
			defaultText = spinnerBuffer.toString().substring(0, spinnerBuffer.toString().length() - 2);

		ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), R.layout.textview_for_spinner, new String[]{defaultText});
		setAdapter(adapterSpinner);
	}

	public void setEmptyTitle(String emptyTitle) {
		this.emptyTitle = emptyTitle;
	}

	public void setSearchHint(String searchHint) {
		this.searchHint = searchHint;
	}

	public class MyAdapter extends BaseAdapter implements Filterable {

		final List<ArrayListContenedor> mOriginalValues; // Original Values
		final LayoutInflater inflater;
		List<ArrayListContenedor> arrayList;

		MyAdapter(Context context, List<ArrayListContenedor> arrayList) {
			this.arrayList = arrayList;
			this.mOriginalValues = arrayList;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
//            //Log.i(TAG, "getView() enter");
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_listview_multiple, parent, false);
				holder.textView = convertView.findViewById(R.id.txt1);
				holder.textView2 = convertView.findViewById(R.id.txt2);
				holder.textView3 = convertView.findViewById(R.id.txt3);
				holder.checkBox = convertView.findViewById(R.id.alertCheckbox);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			//int background = R.color.colorPrimary;
			if (colorSeparation) {
				final int backgroundColor = (position % 2 == 0) ? R.color.colorDarkGray : R.color.colorGray;
			//	background = backgroundColor;
				convertView.setBackgroundColor(ContextCompat.getColor(getContext(), backgroundColor));
			}

			final ArrayListContenedor data = arrayList.get(position);
			if(data.getLote().equals("")) {
				holder.textView.setText(data.getName());
				holder.textView.setTextColor(getResources().getColor(R.color.azul_claro));
			}

			else {
				holder.textView.setText(data.getName());
				holder.textView.setTextColor(getResources().getColor(R.color.azul_claro));

				holder.textView2.setText("Lote: "+data.getLote());
				holder.textView3.setText("Stock: "+data.getCantidad());
				holder.textView2.setVisibility(View.VISIBLE);
				holder.textView3.setVisibility(View.VISIBLE);
			}



			holder.checkBox.setChecked(data.isSelected());

			convertView.setOnClickListener(v -> {
				if (data.isSelected()) { // deselect
					selected--;
				} else {
					selected++;

				}


				final ViewHolder temp = (ViewHolder) v.getTag();
				temp.checkBox.setChecked(!temp.checkBox.isChecked());
				data.setSelected(!data.isSelected());
				//Log.i(TAG, "On Click Selected Item : " + data.getName() + " : " + data.isSelected());
				notifyDataSetChanged();
			});

			if (data.isSelected()) {
				holder.textView.setTextColor(getResources().getColor(R.color.celeste));
				holder.textView2.setTextColor(getResources().getColor(R.color.celeste));
				holder.textView3.setTextColor(getResources().getColor(R.color.celeste));
				if (highlightSelected) {
					holder.textView.setTypeface(null, Typeface.BOLD);
					holder.textView2.setTypeface(null, Typeface.BOLD);
					holder.textView3.setTypeface(null, Typeface.BOLD);

					holder.textView.setTextColor(getResources().getColor(R.color.celeste));
					holder.textView2.setTextColor(getResources().getColor(R.color.celeste));
					holder.textView3.setTextColor(getResources().getColor(R.color.celeste));
				} else {
			//		convertView.setBackgroundColor(android.R.color.darker_gray);
					holder.textView.setTypeface(null, Typeface.BOLD);
					holder.textView2.setTypeface(null, Typeface.BOLD);
					holder.textView3.setTypeface(null, Typeface.BOLD);

					holder.textView.setTextColor(getResources().getColor(R.color.celeste));
					holder.textView2.setTextColor(getResources().getColor(R.color.celeste));
					holder.textView3.setTextColor(getResources().getColor(R.color.celeste));

				}
			} else {
				holder.textView.setTypeface(null, Typeface.NORMAL);
				holder.textView2.setTypeface(null, Typeface.NORMAL);
				holder.textView3.setTypeface(null, Typeface.NORMAL);
				holder.textView2.setTextColor(getResources().getColor(R.color.azul_claro));
				holder.textView3.setTextColor(getResources().getColor(R.color.azul_claro));
				holder.textView.setTextColor(getResources().getColor(R.color.azul_claro));

			}
			holder.checkBox.setTag(holder);

			return convertView;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public Filter getFilter() {
			return new Filter() {

				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {

					arrayList = (List<ArrayListContenedor>) results.values; // has the filtered values
					notifyDataSetChanged();  // notifies the data with new filtered values
				}

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
					List<ArrayListContenedor> FilteredArrList = new ArrayList<>();

					if (constraint == null || constraint.length() == 0) {

						// set the Original result to return
						results.count = mOriginalValues.size();
						results.values = mOriginalValues;
					} else {
						constraint = constraint.toString().toLowerCase();
						for (int i = 0; i < mOriginalValues.size(); i++) {
							//Log.i(TAG, "Filter : " + mOriginalValues.get(i).getName() + " -> " + mOriginalValues.get(i).isSelected());
							String data = mOriginalValues.get(i).getName();
							if (data.toLowerCase().contains(constraint.toString())) {
								FilteredArrList.add(mOriginalValues.get(i));
							}
						}
						// set the Filtered result to return
						results.count = FilteredArrList.size();
						results.values = FilteredArrList;
					}
					return results;
				}
			};
		}

		private class ViewHolder {
			TextView textView;
			TextView textView2;
			TextView textView3;
			CheckBox checkBox;
		}
	}

	public List<ArrayListContenedor> getSelectedItems() {
		List<ArrayListContenedor> selectedItems = new ArrayList<>();
		for (ArrayListContenedor item : items) {
			if (item.isSelected()) {
				selectedItems.add(item);
			}
		}
		return selectedItems;
	}

	public List<Long> getSelectedIds() {
		List<Long> selectedItemsIds = new ArrayList<>();
		for (ArrayListContenedor item : items) {
			if (item.isSelected()) {
				selectedItemsIds.add(item.getId());
			}
		}
		return selectedItemsIds;
	}

}
