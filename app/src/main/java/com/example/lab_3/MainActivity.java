package com.example.lab_3;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView textView10; // Виведення результатів
    private EditText editTextNumberDecimal, editTextNumberDecimal2 , editTextNumberDecimal3, editTextNumberDecimal4, editTextNumberDecimal5; // Поля для введення значень

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ініціалізація елементів
        textView10 = findViewById(R.id.textView10); // Текстове поле для результату
        editTextNumberDecimal = findViewById(R.id.editTextNumberDecimal); // Поле для введення x1
        editTextNumberDecimal2 = findViewById(R.id.editTextNumberDecimal2); // Поле для введення x2
        editTextNumberDecimal3 = findViewById(R.id.editTextNumberDecimal3); // Поле для введення кроку
        editTextNumberDecimal4 = findViewById(R.id.editTextNumberDecimal4); // Поле для введення z
        editTextNumberDecimal5 = findViewById(R.id.editTextNumberDecimal5); // Поле для введення a

        Button buttonCalc = findViewById(R.id.button_calc); // Кнопка для обчислення

        buttonCalc.setOnClickListener(v -> {
            if (!areFieldsValid()) return;
            // Зчитуємо введені значення
            double x1 = Double.parseDouble(editTextNumberDecimal.getText().toString());
            double x2 = Double.parseDouble(editTextNumberDecimal2.getText().toString());
            double z = Double.parseDouble(editTextNumberDecimal4.getText().toString());
            double a = Double.parseDouble(editTextNumberDecimal5.getText().toString());
            double step = Double.parseDouble(editTextNumberDecimal3.getText().toString());

            // Створення результату
            StringBuilder result = new StringBuilder();

            // Обчислення на проміжку
            for (double x = x1; x <= x2; x += step) {
                double denom = Math.exp(x) + Math.pow(a, 5) * Math.atan(x);
                if (denom == 0) {
                    result.append(String.format("x=%.2f -> Помилка: знаменник = 0\n", x));
                } else {
                    double n = computeN(x, z, a);
                    result.append(String.format("x=%.2f -> N=%.5f\n", x, n));
                }
            }

            // Виведення результату
            textView10.setText(result.toString());


        });

        Button buttonClear = findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(v -> {
            textView10.setText("");// Очистити текст
            editTextNumberDecimal.setText("");
            editTextNumberDecimal2.setText("");
            editTextNumberDecimal3.setText("");
            editTextNumberDecimal4.setText("");
            editTextNumberDecimal5.setText("");

        });


        Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(v -> {
            try {
                File file = new File(getFilesDir(), "results.txt");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(textView10.getText().toString().getBytes());
                fos.close();
                Toast.makeText(this, "Результат збережено", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Помилка при збереженні", Toast.LENGTH_SHORT).show();
            }

        });

        Button buttonLoad = findViewById(R.id.button_load);
        buttonLoad.setOnClickListener(v -> {
            try {
                File file = new File(getFilesDir(), "results.txt");
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder text = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line).append("\n");
                }
                br.close();
                textView10.setText(text.toString());
                Toast.makeText(this, "Результат завантажено", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Помилка при зчитуванні", Toast.LENGTH_SHORT).show();
            }

        });



    }
    // Логіка обчислення функції
    private double computeN(double x, double z, double a) {
        double numerator = Math.pow(z, 1.0 / 5) + Math.sqrt(z * x);
        double denominator = Math.exp(x) + Math.pow(a, 5) * Math.atan(x);
        return numerator / denominator;
    }

    private boolean areFieldsValid() {
        if (TextUtils.isEmpty(editTextNumberDecimal.getText()) ||
                TextUtils.isEmpty(editTextNumberDecimal2.getText()) ||
                TextUtils.isEmpty(editTextNumberDecimal4.getText()) ||
                TextUtils.isEmpty(editTextNumberDecimal5.getText()) ||
                TextUtils.isEmpty(editTextNumberDecimal3.getText())) {

            Toast.makeText(this, "Будь ласка, заповніть усі поля", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}