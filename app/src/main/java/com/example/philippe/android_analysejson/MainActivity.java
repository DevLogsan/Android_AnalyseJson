package com.example.philippe.android_analysejson;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity
{
    Button btnSuivant, btnPrecedent;
    EditText edtLibelle, edtNiveau, edtType, edtDescription;
    InputStream is;
    int max, positionCourante=0;

    JSONArray jtab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialisations();

        analyseFichierJSON();

    }

    public void initialisations()
    {
        btnSuivant = findViewById(R.id.btnSuivant);
        btnPrecedent = findViewById(R.id.btnPrecedent);
        edtLibelle = findViewById(R.id.edtLibelle);
        edtNiveau = findViewById(R.id.edtNiveau);
        edtType = findViewById(R.id.edtType);
        edtDescription = findViewById(R.id.edtDescription);

        btnSuivant.setOnClickListener(new EcouteurSuivant());
        btnPrecedent.setOnClickListener(new EcouteurPrecedent());
    }

    public void analyseFichierJSON()
    {
        try
        {
            // on "recupere" le dossier assets
            AssetManager mngr = getAssets();
            // on cree le inputStream
            is = mngr.open("catalogue.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Conversion du BufferedReader en String
            String s;
            StringBuffer texte = new StringBuffer();
            while ((s = br.readLine()) != null) {
                texte.append(s);
                texte.append("\n");
            }
            String texteJSON = texte.toString();

            // Parsing (analyse) du texte texteJSON
            // Création du tableau JSON
            jtab = new JSONArray(texteJSON);
            // max contiendra la valeur de la dernière occurence
            max = jtab.length() - 1;
            positionCourante = 0;
            affichage();
            //edtLibelle.setText(texteJSON);
        }
        catch (JSONException je)
        {
            System.out.println("ERREUR JSON : " + je.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("ERREUR IO : " + e.getMessage());
        }
    }

    public void affichage()
    {
        try
        {
            edtLibelle.setText(jtab.getJSONObject(positionCourante).getString("libelle"));
            edtNiveau.setText(jtab.getJSONObject(positionCourante).getString("niveau"));
            edtType.setText(jtab.getJSONObject(positionCourante).getString("type"));
            edtDescription.setText(jtab.getJSONObject(positionCourante).getString("description"));
        }
        catch (JSONException je)
        {
            System.out.println("ERREUR JSON : " + je.getMessage());
        }
    }

    public class EcouteurSuivant implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if (positionCourante < max)
            {
                positionCourante++;
                affichage();
            }
        }
    }
    
    public class EcouteurPrecedent implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if (positionCourante > 0)
            {
                positionCourante--;
                affichage();
            }
        }
    }
}
