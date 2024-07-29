
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.example.EmailCliente;
import org.example.NotificadorEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotificadorEmailTest {

    @Mock
    private EmailCliente emailClienteMock;
    private NotificadorEmail notificador;

    //Configuracion inicial
    @BeforeEach
    public void setUp(){ notificador = new NotificadorEmail(emailClienteMock);}

    //Test para verificar que se notifica correctamente
    @Test
    public void testNotificar(){
        System.out.println("Ejecutando testNotificar");

        notificador.notificar("ejemplo@test.com", "Hola Mundo");

        verify(emailClienteMock).enviarCorreo("ejemplo@test.com", "Hola Mundo");

        System.out.println("testNotificar realizado correctamente.");
    }

    //Test para verificar que no se envia el correo
    @Test
    public void testNotificarConDireccionVacia(){
        System.out.println("Ejecutando testNotificarConDireccionVacia");

        try{
            notificador.notificar("","Mensaje");
        }catch (IllegalArgumentException e){
            assertEquals("La direccion de correo no puede ser vacia", e.getMessage());
        }
        //Verify
        verify(emailClienteMock, times(0)).enviarCorreo(anyString(),anyString());
        System.out.println("testNotificarConDireccionVacia realizado correctamente.");
    }

    //Test para verificar el comportamiento con mensaje nulo
    @Test
    public void testNotificarConMensajeNulo(){
        System.out.println("Ejecutando testNotificarConMensajeNulo");

        try{
            notificador.notificar("ejemplo@test.com", null);
        }catch (IllegalArgumentException e){
            assertEquals("El mensaje no puede ser nulo", e.getMessage());
        }
        verify(emailClienteMock, times(0)).enviarCorreo(anyString(), anyString());
        System.out.println("testNotificarConMensajeNulo realizado correctamente.");
    }

    //Test para verificar el comportamiento con mensaje largo
    @Test
    public void testNotificarConMensajeLargo(){
        System.out.println("Ejecutando testNotificarConMensajeLargo");

        String longMessage = "a".repeat(1000);
        notificador.notificar("ejemplo@test.com", longMessage);

        verify(emailClienteMock).enviarCorreo("ejemplo@test.com", longMessage);
        System.out.println("testNotificarConMensajeLargo realizado correctamente");
    }

    //Test para verificar el manejo de la excepcion
    @Test
    public void testNotificarConExcepcionDeEnvio(){
        System.out.println("Ejecutando testNotificarConExcepcionDeEnvio");

        doThrow(new RuntimeException("Error al enviar el correo")).when(emailClienteMock).enviarCorreo(anyString(), anyString());
        assertThrows(RuntimeException.class,() ->notificador.notificar("ejemplo@test.com", "Mensaje"));
        System.out.println("Ejecutando testNotificarConExcepcionDeEnvio realizado correctamente.");
    }
}
