package Presentation;

public class Help {
    /**
     * User manual
     */
    private static String Help =
        "<html>"+
        "<body>"+
        "<h1>¡Bienvenido al manual de usuario!</h1>"+
        "<p>En este manual le ofreceremos una explicación detallada del funcionamiento del programa para que pueda utilizar sin ningún tipo de problema todas las funcionalidades de nuestro producto.</p>"+
        "<p></p>"+
        "<h2>1.ENTORNO DE TRABAJO</h2>"+
        "<p>Antes de empezar con la descripción de las funcionalidades vamos a especificar los principales componentes del entorno de trabajo.</p>"+
        "<p>El entorno sobre el que trabajaremos se divide en tres​ <b>partes principales</b>​:</p>"+
        "<p></p>"+
        "<b>1.1.Barra de herramientas</b>"+
        "<p>Donde aparecerán los botones que se usarán para llevar a cabo las principales funcionalidades.</p>"+
        "<p></p>"+
        "<b>1.2.Ventana de propiedades</b>"+
        "<p>Donde aparecerán ciertas propiedades relativas al archivo seleccionado. Esta ventana aparecerá únicamente cuando se haya seleccionado un archivo individual (no carpeta) en la ventana de navegación.</p>"+
        "<p></p>"+
        "<b>1.3.Ventana de navegación</b>"+
        "<p>Donde se elegirá cuál será el archivo del que aparecerán las propiedades en la ventana de propiedades.</p>"+
        "<p></p>"+
        "<p>A parte de estas ventanas principales, a medida que se vayan explorando las distintas funcionalidades del programa el entorno también presenta distintas <b>ventanas emergentes</b>​. Estas son:</p>"+
        "<p></p>"+
        "<b>1.4.Ventana de abrir archivo/carpeta</b>"+
        "<p>Donde se podrá explorar la jerarquía de archivos del sistema y seleccionar el archivo/carpeta que se quiera comprimir/descomprimir. Esta ventana aparecerá al pulsar el botón “Open”.</p>"+
        "<p></p>"+
        "<b>1.5.Ventana de guardado</b>"+
        "<p>Donde se podrá elegir la ubicación en la que se guardará el archivo comprimido/descomprimido. Esta ventana aparecerá al pulsar el botón “Comprimir” si el archivo seleccionado es un archivo no-comprimido y al pulsar el botón “Descomprimir” si el archivo seleccionado es un archivo previamente comprimido.</p>"+
        "<p></p>"+
        "<b>1.6.Ventana de ayuda</b>"+
        "<p>Donde se podrá consultar un breve manual de ayuda. Esta ventana aparecerá al pulsar el botón “Help”.</p>"+
        "<p></p>"+
        "<b>1.7.Ventana de visualización</b>"+
        "<p>Donde podremos previsualizar los archivos individuales (no carpetas) seleccionados en la ventana de navegación. Esta ventana aparecerá al pulsar el botón “Display” o si el archivo seleccionado es una imagen .ppm al pulsar el botón “Lossy”.</p>"+
        "<p></p>"+
        "<p></p>"+
        "<h2>2.FUNCIONALIDADES PRINCIPALES</h2>"+
        "<b>2.1.Comprimir archivo/carpeta</b>"+
        "<p>Para realizar la compresión de un archivo determinado deberemos seguir los siguientes pasos:</p>"+
        "<ul><li>Pulsar el botón “Open” de la barra de herramientas.</li><li>Seleccionar el archivo/carpeta de la ventana emergente de abrir archivo que queremos comprimir (que no sea un archivo previamente comprimido).</li><li>(¡Paso a seguir solo si queremos elegir el algoritmo de compresión y el tamaño del diccionario para un archivo determinado!) Seleccionar el archivo objetivo en la ventana de navegación. Si el archivo no es una imagen, seleccionar el algoritmo con el que se realizará la compresión pulsando en el desplegable “Algorithm”. Si el algoritmo seleccionado no es el LZSS seleccionar el tamaño del diccionario pulsando en el desplegable “Parameter”.</li><li>Pulsar el botón “Compress” de la barra de herramientas.</li><li>Elegir el lugar en el conjunto de archivos del sistema donde se guardará el archivo/carpeta comprimido y con qué nombre lo hará. Una vez pulsado el botón “Save” del explorador empezará la compresión.</li></ul>"+
        "<p></p>"+
        "<b>2.2.Descomprimir archivo</b>"+
        "<p>Para realizar la descompresión de un archivo determinado deberemos seguir los siguientes pasos:</p>"+
        "<ul><li>Pulsar el botón “Open” de la barra de herramientas.</li><li>Seleccionar el archivo/carpeta de la ventana emergente de abrir archivo/carpeta que queremos descomprimir (tiene que ser un archivo/carpeta previamente comprimido).</li><li>Pulsar el botón “Decompress” de la barra de herramientas.</li><li>Elegir el lugar en el conjunto de archivos del sistema donde se guardará el archivo/carpeta descomprimido. Una vez pulsado el botón “Save” del explorador empezará la descompresión.</li></ul>"+
        "<p>¡Atención! El archivo/carpeta comprimido seleccionado en la ventana emergente de abrir archivo/carpeta aparecerá en la ventana de selección de archivo/carpeta con el nombre original y no con el nombre que le hemos otorgado cuando hemos hecho la compresión.</p>"+
        "<p></p>"+
        "<b>2.3.Visualización de estadísticas</b>"+
        "<p>Para visualizar el rendimiento de la compresión de un archivo/carpeta mediante estadísticas deberemos seguir los siguientes pasos:</p>"+
        "<ul><li>Comprimir/descomprimir el archivo/carpeta de la manera explicada en los puntos 2.1 y 2.2.</li><li>Pulsar el botón “Statistics” para visualizar las estadísticas totales.</li><li>Si el hemos comprimido/descomprimido una carpeta seleccionar cada archivo para visualizar las estadísticas a nivel individual en la ventana de propiedades. (si hemos comprimido/descomprimido un archivo individual y lo seleccionamos, nos aparecerá en la ventana de propiedades las mismas estadísticas que pulsando el botón “Statistics”.</li></ul>"+
        "<p>Las estadísticas mostradas contendrán la siguiente información:</p>"+
        "<ul><li>“<b>Compression/decompression ratio</b>”​ : Resulta de la división del tamaño del archivo/carpeta original entre el tamaño del archivo/carpeta comprimido/descomprimido.</li><li>“<b>Space savings</b>”​ : Se define como la reducción de tamaño en relación con el tamaño sin comprimir.</li><li>“<b>Read</b>”​ : Hace referencia al tamaño de datos leídos al realizar la compresión/descompresión</li><li>“<b>Written</b>”​ : Hace referencia al tamaño de datos escritos al realizar la compresión/descompresión.</li><li>“<b>Elapsed time</b>”​ : Hace referencia al tiempo transcurrido mientras se realiza la compresión/descompresión.</li><li>“<b>Compression/decompression per second</b>”/”<b>Speed</b>”​ : Resulta de la división del tamaño del archivo/carpeta comprimido/descomprimido entre el “elapsed time”.</li></ul>"+
        "<p></p>"+
        "<b>2.4.Previsualización de archivos</b>"+
        "<p>Para poder visualizar un archivo individual (no carpeta) en la ventana de visualización sin salir del programa deberemos seguir los siguientes pasos:</p>"+
        "<ul><li>Abrir el archivo a visualizar de manera que aparezca en la ventana de navegación.</li><li>Seleccionar en la ventana de navegación el archivo a visualizar.</li><li>Pulsar el botón “Display” para visualizar el archivo en la ventana de visualización. Si el archivo seleccionado es una imagen sin comprimir, aparecerá también el botón “Lossy” que, al pulsarlo, nos enseñará la imagen resultante después de la compresión y descompresión con el parámetro seleccionado en el desplegable “Parameter”.</li></ul>"+
        "<p></p>"+
        "<b>2.5.Ayuda</b>"+
        "<p>En caso de tener cualquier duda respecto al uso del programa, se puede consultar en la ventana de ayuda un manual integrado en la propia interfaz al pulsar el botón “Help” de la barra de herramientas.</p>"+
        "<p></p>"+
        "<p></p>"+
        "<p></p>"+
        "<p></p>"+
        "<p></p>"+
        "</body>"+
        "</html>";

    /**
     * @return the help
     */
    public static String getHelp() {
        return Help;
    }
}
