package com.medicento.retailerappmedi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

public class TermsAndCondition extends AppCompatActivity {

    TextView updated;

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        updated = findViewById(R.id.term_date);
        pdfView = findViewById(R.id.terms_pdf);

        pdfView.fromAsset("agrement.pdf").load();

    }
}
