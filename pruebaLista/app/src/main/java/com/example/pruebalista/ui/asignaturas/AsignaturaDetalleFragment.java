package com.example.pruebalista.ui.asignaturas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pruebalista.R;
import com.example.pruebalista.clases.Asignatura;

import org.jetbrains.annotations.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AsignaturaDetalleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AsignaturaDetalleFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id Asignatura";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    //
    private String idAsig,nombreAsig,infoAsig;
    private TextView tvId,tvAsig,tvInfo;
    private Button btComprar,btVolver;
    Asignatura asignatura;
    public AsignaturaDetalleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AsignaturaDetalleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AsignaturaDetalleFragment newInstance(int param1, String param2) {
        AsignaturaDetalleFragment fragment = new AsignaturaDetalleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("DETALLE oncrate");

        if (getArguments() != null) {

            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            asignatura = (Asignatura) getArguments().getSerializable("asignatura");
            idAsig = Integer.toString(asignatura.getIdAsignatura());
            nombreAsig = asignatura.getNombre();
            infoAsig = asignatura.getInfo();
            System.out.println("Dato recuperado: id "+idAsig+" Asignatura: "+nombreAsig);
            // Imprimimos, pero en tu caso haz lo necesario
            Log.d("DetalleAsignatura", "El ID: " + idAsig + " Asignatura: "+nombreAsig);

        }
        else {
            System.out.println("Argumentos vacios");
            idAsig = " VACIO ";
        }
        //System.out.println("ID RECIBIDO : "+idAsig+" Asignatura: "+nombreAsig);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_asignatura_detalle, container, false);
        tvId = vista.findViewById(R.id.TvIdAsig);
        tvAsig = vista.findViewById(R.id.TvAsig);
        tvInfo = vista.findViewById(R.id.TvInfoAsig);
        btComprar = vista.findViewById(R.id.btComprar);
        btVolver = vista.findViewById(R.id.btVolver);
        //
        btComprar.setOnClickListener(this);
        btVolver.setOnClickListener(this);

        //
        super.onViewCreated(vista, savedInstanceState);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Y ahora puedes recuperar usando get en lugar de put
        tvId.setText(idAsig);
        tvAsig.setText(nombreAsig);
        tvInfo.setText(infoAsig);
        System.out.println("Dato recuperado");
        // Imprimimos, pero en tu caso haz lo necesario
        Log.d("DetalleAsignatura", "El ID: " + idAsig + " Asignatura: "+nombreAsig);

    }

    @Override
    public void onClick(View v) {
        NavController navController = Navigation.findNavController(v);
        switch (v.getId()){
            case R.id.btComprar:
                System.out.println("COMPRAR CLASES");
                Bundle datosAEnviar = new Bundle();
                // pasamos la asignatura seleccionada
                datosAEnviar.putSerializable("asignatura",asignatura);
                // Preparar el fragmento
                navController.navigate(R.id.action_asignaturaDetalleFragment_to_compaAsigFragment,datosAEnviar);
                break;
            case R.id.btVolver:
                System.out.println("VOLVER ATRAS");
                navController.navigate(R.id.action_asignaturaDetalleFragment_to_nav_asigFragmentVolley);
                break;
        }

    }
}// FIN CLASE ASIGNATURA DETALLE