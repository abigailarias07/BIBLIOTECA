import java.util.Scanner;

public class principal {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        
        // Creamos el usuario usando la clase del archivo estudiante.java
        estudiante usuario = new estudiante();
        
        boolean correoValido = false;
        
        System.out.println("=== BIBLIOTECA CERTUS ===");

        while (!correoValido) {
            System.out.print("Introduce tu correo institucional: ");
            usuario.correo = teclado.nextLine();

            if (usuario.correo.endsWith("@certus.edu.pe")) {
                correoValido = true;
            } else {
                System.out.println("[ALERTA] Correo no permitido. Inténtalo de nuevo.\n");
            }
        }
    
        System.out.print("Introduce tu nombre: ");
        usuario.nombre = teclado.nextLine(); 

        System.out.print("Introduce tu carrera: ");
        usuario.carrera = teclado.nextLine(); 

        System.out.println("\n--- Acceso Permitido y Registro Exitoso ---");
        System.out.println("Nombre: " + usuario.nombre);
        System.out.println("Carrera: " + usuario.carrera); 
        System.out.println("Correo verificado: " + usuario.correo);

        
        libro buscador = new libro(); 
        buscador.mostrarMenuBusqueda(teclado);

        teclado.close();
    }
}
