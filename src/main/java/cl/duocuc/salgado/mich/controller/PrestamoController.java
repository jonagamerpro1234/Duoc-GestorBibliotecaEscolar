package cl.duocuc.salgado.mich.controller;

import cl.duocuc.salgado.mich.model.Prestamo;
import cl.duocuc.salgado.mich.service.PrestamoService;

import java.util.List;

public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController() {
        this.prestamoService = new PrestamoService();
    }

    public void registrar(Prestamo prestamo) {
        prestamoService.registrar(prestamo);
    }

    public List<Prestamo> listar() {
        return prestamoService.listar();
    }

    public void devolver(int idPrestamo) {
        prestamoService.devolver(idPrestamo);
    }

    public void eliminar(int idPrestamo) {
        prestamoService.eliminar(idPrestamo);
    }
}