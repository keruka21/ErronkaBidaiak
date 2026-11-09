package base;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class bidaiakapp {

    static String[] destinos;
    static String[] continentes;
    static String[] climas;
    static int[] turistasAnuales;
    static double[] gastoMedio;
    static double[] seguridad;
    static double[] satisfaccion;
    static double[] transporte;
    static double[] alojamiento;

    static int n; // número de registros cargados
    static final String FICHERO = "DatosViajes.csv";
    static final DecimalFormat df = new DecimalFormat("#0.00");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in, "UTF-8");

        cargarDatosDesdeFichero(FICHERO);

        System.out.println("\nSe han cargado " + n + " registros (sin incluir la cabecera).");

        int opcion;
        do {
            mostrarMenu();
            String linea = sc.nextLine().trim();
            try {
                opcion = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    opcionVisualizacionPorContinente(sc);
                    break;
                case 2:
                    opcionMediaGastoMedio();
                    break;
                case 3:
                    opcionSatisfaccionSuperiorMedia();
                    break;
                case 4:
                    opcionModificarValoresDestino(sc);
                    break;
                case 5:
                    opcionGuardarDatosEnFichero(FICHERO);
                    break;
                case 6:
                    opcionDestinosMasInseguros();
                    break;
                case 7:
                    System.out.println("Fin de la ejecución");
                    break;
                default:
                    System.out.println("Error: La opción de menú introducida no existe.");
            }
        } while (opcion != 7);

        sc.close();
    }

    // ------------------ CARGA DE DATOS ------------------
    public static void cargarDatosDesdeFichero(String ruta) {
        int contador = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), "UTF-8"))) {
            String linea = br.readLine(); // cabecera
            if (linea == null) {
                System.out.println("El fichero está vacío.");
                n = 0;
                return;
            }
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) contador++;
            }
        } catch (IOException e) {
            System.out.println("Error leyendo el fichero: " + e.getMessage());
            n = 0;
            return;
        }

        n = contador;

        destinos = new String[n];
        continentes = new String[n];
        climas = new String[n];
        turistasAnuales = new int[n];
        gastoMedio = new double[n];
        seguridad = new double[n];
        satisfaccion = new double[n];
        transporte = new double[n];
        alojamiento = new double[n];

        int idx = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), "UTF-8"))) {
            String linea = br.readLine(); // saltar cabecera
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                if (p.length < 9) continue;

                destinos[idx] = p[0].trim();
                continentes[idx] = p[1].trim();
                climas[idx] = p[2].trim();
                turistasAnuales[idx] = parseIntSafe(p[3]);
                gastoMedio[idx] = parseDoubleSafe(p[4]);
                seguridad[idx] = parseDoubleSafe(p[5]);
                satisfaccion[idx] = parseDoubleSafe(p[6]);
                transporte[idx] = parseDoubleSafe(p[7]);
                alojamiento[idx] = parseDoubleSafe(p[8]);
                idx++;
            }
        } catch (IOException e) {
            System.out.println("Error leyendo el fichero: " + e.getMessage());
            n = 0;
        }
    }

    private static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private static double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s.replace(',', '.').trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    // ------------------ MENÚ ------------------
    public static void mostrarMenu() {
        System.out.println("\n--- MENÚ ---");
        System.out.println("1. Visualización de datos por continente");
        System.out.println("2. Media de datos parámetro GASTO MEDIO");
        System.out.println("3. Destinos con valor superior a la media en SATISFACCIÓN");
        System.out.println("4. Modificar valores destino");
        System.out.println("5. Guardar datos en fichero");
        System.out.println("6. Destinos más inseguros");
        System.out.println("7. Salir");
        System.out.print("Elige una opción: ");
    }

    // ------------------ OPCIÓN 1 ------------------
    public static void opcionVisualizacionPorContinente(Scanner sc) {
        System.out.print("Introduce el continente: ");
        String cont = sc.nextLine().trim();
        boolean encontrado = false;

        for (int i = 0; i < n; i++) {
            if (continentes[i].equalsIgnoreCase(cont)) {
                if (!encontrado) {
                    System.out.println("\nDestino | Continente | Clima | Turistas | GastoMedio | Seguridad | Satisfacción | Transporte | Alojamiento");
                }
                System.out.println(destinos[i] + " | " + continentes[i] + " | " + climas[i] + " | " + turistasAnuales[i]
                        + " | " + df.format(gastoMedio[i]) + " | " + df.format(seguridad[i]) + " | "
                        + df.format(satisfaccion[i]) + " | " + df.format(transporte[i]) + " | " + df.format(alojamiento[i]));
                encontrado = true;
            }
        }
        if (!encontrado) System.out.println("Error: El continente introducido no existe.");
    }

    // ------------------ OPCIÓN 2 ------------------
    public static double calcularMediaGastoMedio() {
        if (n == 0) return 0.0;
        double suma = 0;
        for (int i = 0; i < n; i++) suma += gastoMedio[i];
        return suma / n;
    }

    public static void opcionMediaGastoMedio() {
        double media = calcularMediaGastoMedio();
        System.out.println("La media del gasto medio para los " + n + " destinos es " + df.format(media) + " euros.");
    }

    // ------------------ OPCIÓN 3 ------------------
    public static double calcularMediaSatisfaccion() {
        double suma = 0;
        for (int i = 0; i < n; i++) suma += satisfaccion[i];
        return suma / n;
    }

    public static void opcionSatisfaccionSuperiorMedia() {
        double media = calcularMediaSatisfaccion();
        System.out.println("La media del parámetro SATISFACCION para los " + n + " destinos es " + df.format(media));
        StringJoiner sj = new StringJoiner(", ");
        for (int i = 0; i < n; i++) {
            if (satisfaccion[i] > media) sj.add(destinos[i] + " " + df.format(satisfaccion[i]));
        }
        System.out.println("Los destinos con valor superior a dicha media son: " + sj.toString());
    }

    // ------------------ OPCIÓN 4 ------------------
    public static void opcionModificarValoresDestino(Scanner sc) {
        System.out.print("Introduce el nombre del destino: ");
        String nombre = sc.nextLine().trim();
        int idx = buscarDestino(nombre);
        if (idx == -1) {
            System.out.println("Error: El destino no existe.");
            return;
        }

        try {
            System.out.print("Introduce Seguridad (0-100): ");
            double seg = leerDoubleEntre(sc, 0, 100);
            System.out.print("Introduce Satisfacción (0-100): ");
            double sat = leerDoubleEntre(sc, 0, 100);
            System.out.print("Introduce Transporte (0-100): ");
            double tra = leerDoubleEntre(sc, 0, 100);
            System.out.print("Introduce Alojamiento (0-100): ");
            double alo = leerDoubleEntre(sc, 0, 100);

            seguridad[idx] = seg;
            satisfaccion[idx] = sat;
            transporte[idx] = tra;
            alojamiento[idx] = alo;
            System.out.println("Valores actualizados correctamente para " + destinos[idx] + ".");
        } catch (InputValidationException e) {
            System.out.println("Error: El valor introducido no es válido.");
        }
    }

    private static int buscarDestino(String nombre) {
        for (int i = 0; i < n; i++) {
            if (destinos[i].equalsIgnoreCase(nombre)) return i;
        }
        return -1;
    }

    private static double leerDoubleEntre(Scanner sc, double min, double max) throws InputValidationException {
        String linea = sc.nextLine().trim();
        double v;
        try {
            v = Double.parseDouble(linea.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new InputValidationException();
        }
        if (v < min || v > max) throw new InputValidationException();
        return v;
    }

    static class InputValidationException extends Exception {}

    // ------------------ OPCIÓN 5 ------------------
    public static void opcionGuardarDatosEnFichero(String ruta) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(ruta), "UTF-8"))) {
            pw.println("Destino;Continente;Clima;TuristasAnuales;GastoMedio;Seguridad;Satisfaccion;Transporte;Alojamiento");
            for (int i = 0; i < n; i++) {
                pw.println(destinos[i] + ";" + continentes[i] + ";" + climas[i] + ";" + turistasAnuales[i] + ";"
                        + df.format(gastoMedio[i]) + ";" + df.format(seguridad[i]) + ";" + df.format(satisfaccion[i])
                        + ";" + df.format(transporte[i]) + ";" + df.format(alojamiento[i]));
            }
            System.out.println("Los datos se han guardado correctamente en el fichero.");
        } catch (IOException e) {
            System.out.println("Error: Los datos no se han guardado en el fichero.");
        }
    }

    // ------------------ OPCIÓN 6 ------------------
    public static void opcionDestinosMasInseguros() {
        if (n == 0) return;
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;
        Arrays.sort(indices, Comparator.comparingDouble(i -> seguridad[i]));
        StringJoiner sj = new StringJoiner(", ");
        int m = Math.min(3, n);
        for (int i = 0; i < m; i++) sj.add(destinos[indices[i]]);
        System.out.println("Los tres destinos más inseguros son: " + sj.toString());
    }
}
