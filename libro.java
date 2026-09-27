import java.util.Scanner;

public class libro {
    // MATRIZ 
    private String[][] catalogo = {
        {"Logica de programación" , "Luis Joyanes" , "programacion"},
        {"El cuerpo humano" , "Alejandra Alvarez" , "medicina"},
        {"Contabilidad financiera" , "Ana Linares" , "contabilidad"},
        {"Algoritmos para la solucion" , "Bernal Quiones" , "programacion"},
        {"Los organos" , "Alejandra Alvarez" , "medicina"},
        {"Hojas de calculo" , "Silvia Villasante" , "contabilidad"} 
    };

    public void mostrarMenuBusqueda(Scanner teclado){
        System.out.println("\n=== BIBLIOTECA CERTUS ===");
        System.out.println("1. BUSCAR POR CATEGORIA");
        System.out.println("2. BUSCAR POR NOMBRE DEL LIBRO");
        System.out.println("3. BUSCAR POR AUTOR DEL LIBRO");
        System.out.print("Selecciona una opción (Escribe 1, 2 o 3): ");

        // CORRECCIÓN: Leemos como texto para que nunca más se rompa el programa
        String opcion = teclado.nextLine(); 
        boolean encontradoGlobal = false; 

        // Evaluamos usando texto en lugar de números
        if (opcion.equals("1")) {
            System.out.print("Escribe la categoría (programacion, medicina, contabilidad): ");
            String buscarCat = teclado.nextLine();
            System.out.println("\n--- Libros que coinciden ---");
            for (int i = 0; i < 6; i++) {
                if (catalogo[i][2].equalsIgnoreCase(buscarCat)) { 
                    System.out.println("- " + catalogo[i][0] + " | Autor: " + catalogo[i][1]);
                    encontradoGlobal = true;
                }
            }

        } else if (opcion.equals("2")) {
            System.out.print("Escribe el nombre del libro o palabra clave: ");
            String buscarNombre = teclado.nextLine();
            System.out.println("\n--- Libros que coinciden ---");
            for (int i = 0; i < 6; i++) {
                if (catalogo[i][0].toLowerCase().contains(buscarNombre.toLowerCase())) { 
                    System.out.println("- " + catalogo[i][0] + " [" + catalogo[i][2] + "]");
                    encontradoGlobal = true;
                }
            }

        } else if (opcion.equals("3")) {
            System.out.print("Escribe el nombre del autor: ");
            String buscarAutor = teclado.nextLine();
            System.out.println("\n--- Libros que coinciden ---");
            for (int i = 0; i < 6; i++) {
                if (catalogo[i][1].toLowerCase().contains(buscarAutor.toLowerCase())) { 
                    System.out.println("- " + catalogo[i][0] + " (Categoría: " + catalogo[i][2] + ")");
                    encontradoGlobal = true;
                }
            }
        } else {
            System.out.println("[ALERTA] Opción inválida.");
        }

        // LÓGICA PARA SEPARAR EL LIBRO Y GENERAR EL CÓDIGO
        if (encontradoGlobal) {
            System.out.print("\n¿Deseas separar alguno de los libros mostrados? (SI / NO): ");
            String respuesta = teclado.nextLine();

            if (respuesta.equalsIgnoreCase("SI")) {
                System.out.print("Escribe el nombre exacto del libro que quieres separar: ");
                String libroAClasificar = teclado.nextLine();
                
                boolean libroExiste = false;
                for (int i = 0; i < 6; i++) {
                    if (catalogo[i][0].equalsIgnoreCase(libroAClasificar)) {
                        libroExiste = true;
                    }
                }

                if (libroExiste) {
                    int codigoAleatorio = (int)(Math.random() * 9000) + 1000;

                    System.out.println("\n=======================================================");
                    System.out.println("                 BIBLIOTECA DE CERTUS                  ");
                    System.out.println("=======================================================");
                    System.out.println("TU CODIGO DE RECOJO ES : CE-" + codigoAleatorio);
                    System.out.println("Libro: " + libroAClasificar);
                    System.out.println("\nHorario de atención de Lunes a Viernes de 10:00 am a 7:00 pm");
                    System.out.println("\n(De no ser recojido en el lapso de 5 dias tu solicitud");
                    System.out.println("sera denegada y tendras que sacar una nueva )");
                    System.out.println("=======================================================");
                    
                    miVentana v = new miVentana();
                    v.setVisible(true);
                    
                } else {
                    System.out.println("[ALERTA] Ese libro no se encuentra en la lista de resultados.");
                }
            } else {
                System.out.println("No se separó ningún libro.");
            }
        } else {
            System.out.println("No se encontraron coincidencias para separar.");
        }
    }
}
