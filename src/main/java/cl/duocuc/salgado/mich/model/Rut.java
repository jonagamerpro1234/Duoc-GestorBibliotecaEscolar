package cl.duocuc.salgado.mich.model;

import cl.duocuc.salgado.mich.util.RutInvalidoException;

public class Rut {

    private final int numero;
    private final char dv;

    public Rut(int numero, char dv) throws RutInvalidoException {
        if (numero <= 0) {
            throw new RutInvalidoException(
                    "El número del RUT debe ser mayor que cero."
            );
        }

        char dvNormalizado = Character.toUpperCase(dv);

        if (!validarDV(numero, dvNormalizado)) {
            throw new RutInvalidoException(
                    "El dígito verificador no corresponde al RUT ingresado."
            );
        }

        this.numero = numero;
        this.dv = dvNormalizado;
    }

    public int getNumero() {
        return numero;
    }

    public char getDv() {
        return dv;
    }

    public static char calcularDV(int numero) throws RutInvalidoException {

        if (numero <= 0) {
            throw new RutInvalidoException("El número del RUT debe ser mayor que cero.");
        }

        int suma = 0;
        int multiplicador = 2;
        int temporal = numero;

        while (temporal > 0) {
            int digito = temporal % 10;
            suma += digito * multiplicador;

            temporal /= 10;
            multiplicador++;

            if (multiplicador > 7) {
                multiplicador = 2;
            }
        }

        int resultado = 11 - (suma % 11);

        if (resultado == 11) {
            return '0';
        }

        if (resultado == 10) {
            return 'K';
        }

        return (char) ('0' + resultado);
    }

    public static boolean validarDV(int numero, char dv)throws RutInvalidoException {
        char dvNormalizado = Character.toUpperCase(dv);
        if (dvNormalizado != 'K'&& (dvNormalizado < '0' || dvNormalizado > '9')) {
            return false;
        }

        return calcularDV(numero) == dvNormalizado;
    }

    @Override
    public String toString() {
        return numero + "-" + dv;
    }
}