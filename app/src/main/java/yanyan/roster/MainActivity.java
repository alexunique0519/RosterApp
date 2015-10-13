package yanyan.roster;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.RadialGradient;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //int birthday_year, birthday_month, birthday_day;
    //static final int DIALOG_CALENDAR = 0;
    final Calendar myCalendar = Calendar.getInstance();

    //data that need to be loaded and saved
    String personName;
    boolean bChecked;
    String birthday;
    String eyeColor;
    String shirtSizeDes;
    int pantsSize;
    int shirtSize;
    int shoeSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //retrieve data from sharedPreference
        retrieveData();

        //initialize all the UI components using the loaded data
        InitUI();

        //for setting birthday
        subscribeCalendarChangeEvent();
        subscribeRadioGroupChangeEvent();
        subscribeNameEditTextChangeEvent();
        subscribeSpinnerSelectItemChangeEvent();
        subscribeSeekBarChangeEvent(R.id.seekBar_pantSize);
        subscribeSeekBarChangeEvent(R.id.seekBar_shirtSize);
        subscribeSeekBarChangeEvent(R.id.seekBar_shoeSize);

        //set this after subscribe the seekBarProgressChangeEvent
        setSeekInitialPostion();

    }

    private void setSeekInitialPostion(){
        SeekBar sb_pants = (SeekBar)findViewById(R.id.seekBar_pantSize);
        sb_pants.setProgress(pantsSize);

        SeekBar sb_shirt = (SeekBar)findViewById(R.id.seekBar_shirtSize);
        sb_shirt.setProgress(shirtSize);

        SeekBar sb_shoe = (SeekBar)findViewById(R.id.seekBar_shoeSize);
        sb_shoe.setProgress(shoeSize - 4);

        if(shoeSize == 4){
            TextView tv = (TextView)findViewById(R.id.textView_shoeSize);
            tv.setText(String.valueOf(shoeSize));
        }
    }

    private void retrieveData(){
        //get data from sharedPreference
        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);

        personName = prefs.getString("personName", "");
        bChecked = prefs.getInt("isChecked", 0) == 1 ? true : false;
        birthday = prefs.getString("birthday", "");
        eyeColor = prefs.getString("eyeColor", "");
        shirtSizeDes = prefs.getString("shirtSizeDes", "");
        pantsSize = prefs.getInt("pantsSize", 0);
        shirtSize = prefs.getInt("shirtSize", 0);
        shoeSize = prefs.getInt("shoeSize", 0);

    }

    private void InitUI(){

        EditText et = (EditText)findViewById(R.id.editText_name);
        et.setText(personName);

        CheckBox cb = (CheckBox)findViewById(R.id.checkBox);
        cb.setChecked(bChecked);

        TextView tv_birthday = (TextView)findViewById(R.id.textView_birthday);
        tv_birthday.setText("Birthday: " + birthday);

        //init spinner
        initSpinner();

        Spinner spn = (Spinner)findViewById(R.id.spinner_eyeColor);
        switch (eyeColor){
            case "Amber":
                spn.setSelection(1);
                break;
            case "Blue":
                spn.setSelection(2);
                break;
            case "Brown":
                spn.setSelection(3);
                break;
            case "Gray":
                spn.setSelection(4);
                break;
            case "Green":
                spn.setSelection(5);
                break;
            case "Hazel":
                spn.setSelection(6);
                break;
            default:
                spn.setSelection(0);
                break;
        }

        RadioGroup rg = (RadioGroup)findViewById(R.id.radiogroup_1);
        switch (shirtSizeDes){
            case "X":
                rg.check(R.id.radioButton_xs);
                break;
            case "S":
                rg.check(R.id.radioButton_s);
                break;
            case "M":
                rg.check(R.id.radioButton_m);
                break;
            case "L":
                rg.check(R.id.radioButton_l);
                break;
            case "XL":
                rg.check(R.id.radioButton_xl);
                break;
            case "XXL":
                rg.check(R.id.radioButton_xxl);
                break;
            default:
                rg.clearCheck();
                break;
        }


    }

    private  void subscribeNameEditTextChangeEvent(){
        EditText et = (EditText)findViewById(R.id.editText_name);
        et.addTextChangedListener(new TextWatcher() {
                                      @Override
                                      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                      }

                                      @Override
                                      public void onTextChanged(CharSequence s, int start, int before, int count) {

                                      }

                                      @Override
                                      public void afterTextChanged(Editable s) {
                                          personName = s.toString();
                                      }
                                  }

        );
    }


    //subscribe calendar change event
    private void subscribeCalendarChangeEvent(){

        TextView tv = (TextView)findViewById(R.id.button_setBirthday);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                };

                new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    //initialize the spinner for selecting eye color
    private void initSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.spinner_eyeColor);
        ArrayAdapter<String> adapter;
        final List<String> list;

        list = new ArrayList<String>();
        list.add("Choose a color");
        list.add("Amber");
        list.add("Blue");
        list.add("Brown");
        list.add("Gray");
        list.add("Green");
        list.add("Hazel");

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private  void subscribeSpinnerSelectItemChangeEvent(){
         Spinner spn = (Spinner)findViewById(R.id.spinner_eyeColor);
         spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 switch(position){
                     case 0:
                         eyeColor = "";
                         break;
                     case 1:
                         eyeColor = "Amber";
                         break;
                     case 2:
                         eyeColor = "Blue";
                         break;
                     case 3:
                         eyeColor = "Brown";
                         break;
                     case 4:
                         eyeColor = "Gray";
                         break;
                     case 5:
                         eyeColor = "Green";
                         break;
                     case 6:
                         eyeColor = "Hazel";
                         break;
                     default:
                         eyeColor = "";
                         break;
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });
    }

    private  void subscribeRadioGroupChangeEvent()
    {
        RadioGroup rg = (RadioGroup)findViewById(R.id.radiogroup_1);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radioButton_xs:
                        shirtSizeDes = "XS";
                        break;
                    case R.id.radioButton_s:
                        shirtSizeDes = "S";
                        break;
                    case R.id.radioButton_m:
                        shirtSizeDes = "M";
                        break;
                    case R.id.radioButton_l:
                        shirtSizeDes = "L";
                        break;
                    case R.id.radioButton_xl:
                        shirtSizeDes = "XL";
                        break;
                    case R.id.radioButton_xxl:
                        shirtSizeDes = "XXL";
                        break;
                    default:
                        shirtSizeDes = "";
                        break;
                }
            }
        });
    }

    //
    private  void subscribeSeekBarChangeEvent(final int resID){
        SeekBar sb = (SeekBar)findViewById(resID);
        sb.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        String s = String.valueOf(progress_value);
                        switch ( resID ){
                            case R.id.seekBar_pantSize:{
                                pantsSize = progress_value;

                                TextView tv = (TextView)findViewById(R.id.textView_pantsSize);
                                tv.setText(s);
                            }
                            break;
                            case R.id.seekBar_shirtSize:{
                                shirtSize = progress_value;

                                TextView tv = (TextView)findViewById(R.id.textView_shirtSize);
                                tv.setText(s);

                            }
                            break;
                            case R.id.seekBar_shoeSize:{
                                shoeSize = progress_value + 4;
                                TextView tv = (TextView)findViewById(R.id.textView_shoeSize);
                                s = String.valueOf(shoeSize);
                                tv.setText(s);
                            }
                            default:
                                break;
                        }

                        //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
    }

    //update the birthday label after selection date
    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        TextView tv = (TextView)findViewById(R.id.textView_birthday);
        tv.setText( "Birthday: " + sdf.format(myCalendar.getTime()));
        birthday = sdf.format(myCalendar.getTime());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSave(View view) {

        //get the sharepreference instance
        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("personName", personName);

        CheckBox cb = (CheckBox)findViewById(R.id.checkBox);
        bChecked = cb.isChecked();
        editor.putInt("isChecked", bChecked ? 1 : 0);
        editor.putString("birthday", birthday);
        editor.putString("eyeColor", eyeColor);
        editor.putString("shirtSizeDes", shirtSizeDes);
        editor.putInt("pantsSize", pantsSize);
        editor.putInt("shirtSize", shirtSize);
        editor.putInt("shoeSize", shoeSize);

        boolean bRes = editor.commit();
        if(bRes)
            Toast.makeText(MainActivity.this, "Data Saved successfully.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "Data failed to save.", Toast.LENGTH_SHORT).show();
    }
}
