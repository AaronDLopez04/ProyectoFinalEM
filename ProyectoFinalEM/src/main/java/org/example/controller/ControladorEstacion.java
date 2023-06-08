package org.example.controller;

import org.example.model.EstacionMetro;
import org.example.model.ModeloTablaEstacionMetro;
import org.example.view.Ventana;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class ControladorEstacion extends MouseAdapter {
    private Ventana view;
    private ModeloTablaEstacionMetro modelo;

    public ControladorEstacion(Ventana view) {
        this.view = view;
        this.view.getBtnCargar().addMouseListener(this);
        this.view.getBtnAgregar().addMouseListener(this);
        this.view.getBtnEliminar().addMouseListener(this);
        this.view.getBtnActualizar().addMouseListener(this);
        this.view.getTabla().addMouseListener(this);
        modelo = new ModeloTablaEstacionMetro();
        this.view.getTabla().setModel(modelo);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == this.view.getBtnAgregar()){
            modelo.cargarDatos();
            this.view.getTabla().setModel(modelo);
            this.view.getTabla().updateUI();

        }
        if(e.getSource() == this.view.getBtnAgregar()){

            EstacionMetro estacion = new EstacionMetro();
            estacion.setId(0);
            estacion.setNombre(this.view.getTxtNombre().getText());
            estacion.setLinea(this.view.getTxtLinea().getText());
            estacion.setColor(this.view.getTxtColor().getText());
            estacion.setUbicacion(this.view.getTxtUbicacion().getText());
            estacion.setUrl(this.view.getTxtUrl().getText());

            if(modelo.agregar(estacion)){
                JOptionPane.showMessageDialog(view,"Se agrego correctamente","Confirmacion",JOptionPane.INFORMATION_MESSAGE);

                modelo.cargarDatos();
                this.view.getTabla().setModel(modelo);
                this.view.getTabla().updateUI();

            }else {
                JOptionPane.showMessageDialog(view,"No se pudo agregar correctamente, revise su informacion","Error",JOptionPane.ERROR_MESSAGE);
            }
            this.view.limpiar();

        }

        if (e.getSource() == this.view.getTabla()){

            try {
                int index = this.view.getTabla().getSelectedRow();
                EstacionMetro tmp = modelo.getEstacionAtIndex(index);
                this.view.getImage().setIcon(tmp.getImagen());
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
            this.view.getImage().setText("");
        }

        if(e.getSource() == this.view.getBtnEliminar()){
            int index = this.view.getTabla().getSelectedRow();
            EstacionMetro tmp = modelo.getEstacionAtIndex(index);
            int resultado = JOptionPane.showConfirmDialog(view,"Esta seguro que desea borrar el contenido "+(tmp.getId())+"?","Confirmacion",JOptionPane.YES_NO_OPTION);
            if(resultado == JOptionPane.YES_NO_OPTION) {
                if (modelo.eliminarDatos(String.valueOf(tmp.getId()))) {
                    JOptionPane.showMessageDialog(view, "Se borro correctamente", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    modelo.cargarDatos();
                    this.view.getTabla().setModel(modelo);
                    this.view.getTabla().updateUI();


                } else {
                    JOptionPane.showMessageDialog(view, "No se pudo borrar correctamente, revise su conexion", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

        }

        if (e.getSource() == this.view.getBtnActualizar()) {
            int index = this.view.getTabla().getSelectedRow();
            EstacionMetro tmp = modelo.getEstacionAtIndex(index);
            String id = String.valueOf(tmp.getId());
            String datoActualizado = JOptionPane.showInputDialog(view,"Inserte el cambio a actualizar de "+
                    this.view.getTabla().getColumnName(view.getTabla().getSelectedColumn())+" del registro "
                    +id,"Actualizar",JOptionPane.INFORMATION_MESSAGE);


            int colIndex = this.view.getTabla().getSelectedColumn();

            System.out.println(colIndex+""+datoActualizado+""+id);

            try {
                if(modelo.actualizarDatos(datoActualizado, Integer.parseInt(id),colIndex)){
                    System.out.println("se realizo con exito");
                    modelo.cargarDatos();
                    this.view.getTabla().setModel(modelo);
                    this.view.getTabla().updateUI();

                }else{
                    System.out.println("no se realizo con exito");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }


        }
    }
}