import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorMonedas {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/f5e44e3540a5f437a7647b5f/latest/USD";

    public static void main(String[] args) {
        try {
            // Llamar a la API y obtener las tasas de cambio
            String respuesta = obtenerRespuestaAPI(API_URL);
            JSONObject jsonResponse = new JSONObject(respuesta);
            JSONObject tasas = jsonResponse.getJSONObject("conversion_rates");

            // Scanner para la entrada del usuario
            Scanner scanner = new Scanner(System.in);
            int opcion;

            do {
                // Mostrar menú
                mostrarMenu();
                opcion = scanner.nextInt();

                if (opcion >= 1 && opcion <= 6) {
                    System.out.print("¿Cuánto desea cambiar? ");
                    double cantidad = scanner.nextDouble();
                    ResultadoConversion resultado = realizarConversion(opcion, cantidad, tasas);
                    System.out.printf("Resultado: %.2f %s\n", resultado.valor, resultado.moneda);
                }
            } while (opcion != 7);

            scanner.close();
            System.out.println("¡Gracias por usar el conversor de monedas!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Clase interna para almacenar el resultado de la conversión
    static class ResultadoConversion {
        double valor;
        String moneda;

        ResultadoConversion(double valor, String moneda) {
            this.valor = valor;
            this.moneda = moneda;
        }
    }

    // Método para mostrar el menú de opciones
    private static void mostrarMenu() {
        System.out.println("***********************************************");
        System.out.println("Bienvenido al Conversor de Monedas:");
        System.out.println("1: De Dólar a Peso Argentino");
        System.out.println("2: Peso Argentino a Dólar");
        System.out.println("3: De Dólar a Real Brasileño");
        System.out.println("4: De Real Brasileño a Dólar");
        System.out.println("5: De Dólar a Peso Colombiano");
        System.out.println("6: De Peso Colombiano a Dólar");
        System.out.println("7: Salir");

        System.out.println("Elija una opción válida:");

        System.out.println("***********************************************");
    }

    // Método para obtener la respuesta de la API
    private static String obtenerRespuestaAPI(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Leer la respuesta de la API
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    // Método para realizar la conversión
    private static ResultadoConversion realizarConversion(int opcion, double cantidad, JSONObject tasas) {
        double tasa;
        String moneda = "";

        // Seleccionar la tasa según la opción
        switch (opcion) {
            case 1: // Dólar a Peso Argentino
                tasa = tasas.getDouble("ARS");
                return new ResultadoConversion(cantidad * tasa, "ARS");
            case 2: // Peso Argentino a Dólar
                tasa = tasas.getDouble("ARS");
                return new ResultadoConversion(cantidad / tasa, "USD");
            case 3: // Dólar a Real Brasileño
                tasa = tasas.getDouble("BRL");
                return new ResultadoConversion(cantidad * tasa, "BRL");
            case 4: // Real Brasileño a Dólar
                tasa = tasas.getDouble("BRL");
                return new ResultadoConversion(cantidad / tasa, "USD");
            case 5: // Dólar a Peso Colombiano
                tasa = tasas.getDouble("COP");
                return new ResultadoConversion(cantidad * tasa, "COP");
            case 6: // Peso Colombiano a Dólar
                tasa = tasas.getDouble("COP");
                return new ResultadoConversion(cantidad / tasa, "USD");
            default:
                return new ResultadoConversion(0, "");
        }
    }
}
