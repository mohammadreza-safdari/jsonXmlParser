package com.project_four.jsonxmlparser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.project_four.jsonxmlparser.model.Country;
import com.project_four.jsonxmlparser.model.CountryForGSONLibrary;
import com.project_four.jsonxmlparser.model.Employee;
import com.project_four.jsonxmlparser.parser.JsonParser;
import com.project_four.jsonxmlparser.parser.XmlParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_pull_parser_XML, btn_parse_JSON, btn_jdom_parser_XML, btn_clear, btn_GSON;
    ListView data_lv;
    private List<Employee> employees;
    private List<Country> countries;
    private ArrayAdapter<Employee> employeeArrayAdapter;
    private ArrayAdapter<Country> countryArrayAdapter;
    private Country[] countries_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        btn_parse_JSON.setOnClickListener(MainActivity.this);
        btn_GSON.setOnClickListener(MainActivity.this);
        btn_pull_parser_XML.setOnClickListener(MainActivity.this);
        btn_jdom_parser_XML.setOnClickListener(MainActivity.this);
        btn_clear.setOnClickListener(MainActivity.this);
    }

    private void setupViews() {
        btn_pull_parser_XML = (Button) findViewById(R.id.btn_pull_parser_XML);
        btn_jdom_parser_XML = (Button) findViewById(R.id.btn_jdom_parser_XML);
        btn_parse_JSON = (Button) findViewById(R.id.btn_parse_JSON);
        btn_GSON = (Button) findViewById(R.id.btn_GSON);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        data_lv = (ListView) findViewById(R.id.data_lv);
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        switch (Id){
            case R.id.btn_parse_JSON:
                countries = new JsonParser().jsonParser(getResources().openRawResource(R.raw.countries));
                uiUpdate(countries, "countries");
                break;
            case R.id.btn_GSON:
                CountryForGSONLibrary countryForGSONLibrary = new JsonParser().jsonParserUsingGSON(MainActivity.this);
                countries = countryForGSONLibrary.getCountries();
                /*
                    if use arrays in CountryForGSONLibrary :
                    countries_array = countryForGSONLibrary.getCountries();
                    countries = new ArrayList<>();
                    for (Country country : countries_array)
                    countries.add(country);
                 */
                uiUpdate(countries, "countries");
                break;
            case R.id.btn_pull_parser_XML:
                employees = new XmlParser(MainActivity.this).xmlPullParser();
                uiUpdate(employees, "employees");
                break;
            case R.id.btn_jdom_parser_XML:
                employees = new XmlParser(MainActivity.this).jdomXmlParser();
                uiUpdate(employees, "employees");
                break;
            case R.id.btn_clear:
                data_lv.setAdapter(null);
                break;
            default:
                break;
        }
    }

    private void uiUpdate(List list, String type){
        if (list == null)
            list = new ArrayList<>();
        if (type.equals("employees")){
            employeeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            data_lv.setAdapter(employeeArrayAdapter);
        }else if (type.equals("countries")){
            countryArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            data_lv.setAdapter(countryArrayAdapter);
        }
    }
}