package edu.fich.pfcii.snoredetection;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.fich.pfcii.snoredetection.db.DatabaseManagerSnore;
import edu.fich.pfcii.snoredetection.helper.Helper;

/**
 * Created by Leandro on 05/12/2016.
 */
public class tabs extends AppCompatActivity {

    private TextView txtTab1;
    private TextView txtTab2;

    private BarChart barchart;
    private ArrayList<BarEntry> entradas = new ArrayList<>();
    private ArrayList<String> etiquetas = new ArrayList<String>();
    private ArrayList<Double> amplitudes = new ArrayList<Double>();
    private float[] muestras;


    private PieChart piechart;
    private PieChart piechart2;

    private ListView listview;

    private Button btnPDF;

    private Cursor cursor;
    private DatabaseManagerSnore manager;
    private Helper helper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);



        txtTab1 = (TextView) findViewById(R.id.textView1);
        txtTab2 = (TextView) findViewById(R.id.textView2);


        //--------------------------------------------------------
        //
        //----------Contenido para la tab 1 (gráficos)------------
        //
        //--------------------------------------------------------

        //Contenido de los gráficos hardcodeados!!!!
        final Date horaActual = new Date();
        final Calendar calendar = Calendar.getInstance();
        final Calendar calinicio = Calendar.getInstance();
        final Calendar diferencia = Calendar.getInstance();
        calinicio.setTime(horaActual);
        calendar.setTime(horaActual);
        final SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");

        cursor = this.manager.cargarCursor();
        cursor.moveToPosition(0);
        String amplitud= cursor.getString(4);

        amplitudes = helper.getDoubleFromString(amplitud);

        muestras = new float[12];
        int [] barColorArray1 = new int[12];

        //muestras[0] = amplitudes.get(0).floatValue(); // 0.20f;
        //muestras[1] = amplitudes.get(1).floatValue(); //0.25f;
        muestras[0] = 0.20f;
        muestras[1] = 0.25f;
        muestras[2] = 0.22f;
        muestras[3] = 0.30f;
        muestras[4] = 0.35f;
        muestras[5] = 0.60f;
        muestras[6] = 0.21f;
        muestras[7] = 0.22f;
        muestras[8] = 0.26f;
        muestras[9] = 0.60f;
        muestras[10] = 0.90f;
        muestras[11] = 0.20f;

        //--------------Calculo de porcentajes------------------
        int cant_ron = 0;
        int cant_ron_simple = 0;
        int cant_ron_alto = 0;

        //seteo los colores de las barras segun intensidad de tres niveles HARCODEADO POR AHORA.
        for (int i=0; i<muestras.length; i++) {
            float valor = muestras[i];
            entradas.add(new BarEntry(valor, i));
            etiquetas.add(formato.format(calendar.getTime())+" ");

            if (valor<0.30) {
                //para segmentos de no ronquidos
                barColorArray1[i] = Color.CYAN;
            }else {
                cant_ron++;
                if (valor >= 0.30 && valor < 0.40) {
                    //nivel simple menor a 40% de la energía
                    barColorArray1[i] = Color.YELLOW;
                    cant_ron_simple++;
                } else {
                    //nivel alto mayor al 40% de la energia
                    barColorArray1[i] = Color.RED;
                    cant_ron_alto++;
                }
            }
            calendar.add(calendar.MINUTE,5);
        }

        //porcentaje de segmentos con ronquidos
        float por_ron = (cant_ron/12f)*100f;
        //porcentaje de segmentos sin ronquidos
        float por_noron = 100-por_ron;

        //porcentaje de ronquidos distribuidos por intensidades
        float por_ron_simple = (cant_ron_simple/(float)cant_ron)*100f;
        float por_ron_alto = 100-por_ron_simple;

        //-------------BAR CHART-------------------------
        int [] colorLegend = new int[3];
        colorLegend[0] = Color.CYAN;
        colorLegend[1] = Color.YELLOW;
        colorLegend[2] = Color.RED;

        BarDataSet dataset = new BarDataSet(entradas, "# de segmento");
        dataset.setColors(barColorArray1);
        dataset.setDrawValues(false);
        BarData datos = new BarData(etiquetas, dataset);

        barchart = (BarChart) findViewById(R.id.barchart);
        barchart.setDescription("");

        Legend legend = barchart.getLegend();
        legend.setCustom(colorLegend, new String[]{"No ronquidos", "Simple", "Alto"});
        barchart.setData(datos);

        //-------------PIE CHART 1-----------------------
        float[] yData = { por_ron, por_noron };
        String[] xData = { "Ronquidos", "No ronquidos" };

        int [] pieColorArray1 = new int[2];
        pieColorArray1[0] = Color.GRAY;
        pieColorArray1[1] = Color.CYAN;

        piechart = (PieChart) findViewById(R.id.piechart);
        // creating data values
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < yData.length; i++)
            entries.add(new Entry(yData[i], i));

        PieDataSet datasetpie = new PieDataSet(entries, "Porcentaje");
        datasetpie.setColors(pieColorArray1); // set the color

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            labels.add(xData[i]);

        PieData data = new PieData(labels, datasetpie);
        piechart.setDescription("");
        piechart.setData(data);

        piechart.setDescription("% de segmentos con ronquidos");  // set the description

        //-------------PIE CHART 2-----------------------
        float[] yData2 = { por_ron_simple, por_ron_alto };
        String[] xData2 = { "Simple", "Alto" };

        int [] pieColorArray2 = new int[3];
        pieColorArray2[0] = Color.YELLOW;
        pieColorArray2[1] = Color.RED;

        piechart2 = (PieChart) findViewById(R.id.piechart2);
        // creating data values
        ArrayList<Entry> entries2 = new ArrayList<>();

        for (int i = 0; i < yData2.length; i++)
            entries2.add(new Entry(yData2[i], i));

        PieDataSet datasetpie2 = new PieDataSet(entries2, "");
        datasetpie2.setColors(pieColorArray2); // set the color

        // creating labels
        ArrayList<String> labels2 = new ArrayList<String>();

        for (int i = 0; i < xData2.length; i++)
            labels2.add(xData2[i]);

        PieData data2 = new PieData(labels2, datasetpie2);
        piechart2.setDescription("");
        piechart2.setData(data2);

        piechart2.setDescription("% de segmentos para cada intensidad");  // set the description

        //---------------------------------------------------------
        //
        //---------Contenido para la tab 2 (reportes)--------------
        //
        //---------------------------------------------------------

        //---------------GridView para mostrar el reporte textual---------------
        calinicio.setTime(horaActual);
        calendar.setTime(horaActual);

        String[] salida = new String[12];
        int canSegRonquidos=0;

        for(int i=0; i<12; i++) {
            float valor=muestras[i];
            if (valor<0.30) {
                salida[i] = formato.format(calendar.getTime())+"  --  No ronquidos";
            }else {
                canSegRonquidos++;
                if (valor >= 0.30 && valor < 0.40) {
                    salida[i] = formato.format(calendar.getTime())+"  --  Simple";
                } else {
                    salida[i] = formato.format(calendar.getTime())+"  --  Alto";
                }
            }
            calendar.add(calendar.MINUTE,5);
        }

        diferencia.setTimeInMillis(calendar.getTime().getTime() - calinicio.getTime().getTime());

        String horaInicio =  "Hora de inicio de captura: " +formato.format(horaActual);
        String horaFin = "Hora de finalización: "+formato.format(calendar.getTime());
        String duracion = "Duración: 1 hora";
        String cantidadSegmentos = "Cantidad de segmentos con ronquido:" +canSegRonquidos;
        String mayorCantidad = "Hora con mayor cantidad de ronquidos: "+formato.format(horaActual)+"--"+formato.format(calendar.getTime());
        String promedioRonquidos = "Promedio de ronquidos/hora: 5 ronquidos por hora";

        txtTab1.setText(horaInicio+"\n"+horaFin+"\n"+duracion+"\n"+cantidadSegmentos+"\n"+mayorCantidad+"\n"+promedioRonquidos+"\n");
        //txtTab2.setText(horaInicio+"\n"+horaFin+"\n"+duracion+"\n"+cantidadSegmentos+"\n"+mayorCantidad+"\n"+promedioRonquidos+"\n");

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this, R.layout.listitem, salida);

        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adaptador);

        //----------------------------------------------------------
        //
        //---------Contenido para la generación PDF---------------
        //
        //----------------------------------------------------------
        btnPDF = (Button) findViewById(R.id.btnPdf);

        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Guardo las imagenes de los gráficos
                barchart.saveToGallery("barchart.jpg", 85);
                piechart.saveToGallery("piechart1.jpg",85);
                piechart2.saveToGallery("piechart2.jpg",85);

                try {
                    //creo el pdf con los datos
                    Document documento = new Document();

                    //abro el archivo de la EDF
                    File ruta_sd = Environment.getExternalStorageDirectory();
                    File f = new File(ruta_sd.getAbsolutePath(), "reporte.pdf");
                    FileOutputStream ficheroPdf = new FileOutputStream(f);
                    PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
                    Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

                    documento.open();

                    //Inserto datos personales
                    String nombre = "Jose";
                    String apellido = "Lopez";
                    String edad = "28";
                    String sexo = "Masculino";
                    documento.add(new Paragraph("Datos del paciente", boldFont));
                    documento.add(new Paragraph("Nombre: "+nombre));
                    documento.add(new Paragraph("Apellido: "+apellido));
                    documento.add(new Paragraph("Edad: "+edad));
                    documento.add(new Paragraph("Sexo: "+sexo));

                    // Insertamos una tabla con los resultados del análisis.
                    PdfPTable tabla = new PdfPTable(2);

                    String horacomienzo = "Hora de comienzo del segmento";
                    String intensidad = "Intensidad";

                    tabla.addCell(horacomienzo);
                    tabla.addCell(intensidad);

                    calinicio.setTime(horaActual);
                    calendar.setTime(horaActual);

                    int canSegRonquidos=0;

                    for(int i=0; i<12; i++) {
                        float valor=muestras[i];
                        if (valor<0.30) {
                            tabla.addCell(formato.format(calendar.getTime()));
                            tabla.addCell("No ronquidos");
                        }else {
                            canSegRonquidos++;
                            if (valor >= 0.30 && valor < 0.40) {
                                tabla.addCell(formato.format(calendar.getTime()));
                                tabla.addCell("Simple");
                            } else {
                                tabla.addCell(formato.format(calendar.getTime()));
                                tabla.addCell("Alto");
                            }
                        }
                        calendar.add(calendar.MINUTE,5);
                    }

                    diferencia.setTimeInMillis(calendar.getTime().getTime() - calinicio.getTime().getTime());

                    String horaInicio =  "Hora de inicio de captura: " +formato.format(horaActual);
                    String horaFin = "Hora de finalización: "+formato.format(calendar.getTime());
                    String duracion = "Duración: 1 hora";
                    String cantidadSegmentos = "Cantidad de segmentos con ronquido:" +canSegRonquidos;
                    String mayorCantidad = "Hora con mayor cantidad de ronquidos: "+formato.format(horaActual)+"--"+formato.format(calendar.getTime());
                    String promedioRonquidos = "Promedio de ronquidos/hora: 5 ronquidos por hora";

                    PdfPTable tablaheader = new PdfPTable(1);
                    tablaheader.addCell(horaInicio);
                    tablaheader.addCell(horaFin);
                    tablaheader.addCell(duracion);
                    tablaheader.addCell(cantidadSegmentos);
                    tablaheader.addCell(mayorCantidad);
                    tablaheader.addCell(promedioRonquidos);
                    tablaheader.setSpacingBefore(50);
                    tablaheader.setSpacingAfter(50);
                    tabla.setSpacingAfter(50);
                    documento.add(new Paragraph("Detalle del análisis", boldFont));
                    documento.add(tablaheader);
                    documento.add(tabla);

                    //Inserto las imágenes de los gráficos en el PDF
                    Image image1 = Image.getInstance(ruta_sd.getAbsolutePath()+"/DCIM/barchart.jpg");
                    Image image2 = Image.getInstance(ruta_sd.getAbsolutePath()+"/DCIM/piechart1.jpg");
                    Image image3 = Image.getInstance(ruta_sd.getAbsolutePath()+"/DCIM/piechart2.jpg");

                    image1.setAlignment(Element.ALIGN_CENTER);
                    image2.setAlignment(Element.ALIGN_CENTER);
                    image3.setAlignment(Element.ALIGN_CENTER);

                    image1.scalePercent(80f);
                    image2.scalePercent(55f);
                    image3.scalePercent(55f);

                    documento.newPage();
                    documento.add(new Paragraph("Gráficos", boldFont));
                    documento.add(image1);
                    documento.add(image2);
                    documento.add(image3);

                    documento.close();

                    // Reemplazamos el email por el del médico
                    String[] to = { "leandroperren@gmail.com"};
                    String[] cc = { "" };
                    enviar(to, cc, "Envio PDF",
                            "Resultado del análisis de SnoreDetection");

                } catch (Exception e) { //Catch de excepciones
                    System.err.println("Ocurrio un error: " + e.getMessage());
                }

            }
        });


        //---------------Seteo las tabs------------------------------
        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("GRAFICOS");
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("REPORTE");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);
    }

    private void enviar(String[] to, String[] cc,
                        String asunto, String mensaje) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        //String[] to = direccionesEmail;
        //String[] cc = copias;
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);

        String sdCardRoot = Environment.getExternalStorageDirectory().getPath();
        String fullFileName = sdCardRoot + File.separator  + "reporte.pdf";
        Uri uri = Uri.fromFile(new File(fullFileName));
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.setType("application/pdf");

        //emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email "));
    }

}
