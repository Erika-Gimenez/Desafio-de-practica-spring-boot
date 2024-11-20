package com.aluracursos.desafiodepracticaspringboot.principal;


import com.aluracursos.desafiodepracticaspringboot.molde.DatosLibro;
import com.aluracursos.desafiodepracticaspringboot.molde.DatosResultado;
import com.aluracursos.desafiodepracticaspringboot.service.ConsumoApi;
import com.aluracursos.desafiodepracticaspringboot.service.DeserializacionDatosApi;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Principal {
    private ConsumoApi consumoApi = new ConsumoApi();
    private DeserializacionDatosApi deserializar = new DeserializacionDatosApi();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final String BUSCAR = "?search=";
    private final Scanner leer = new Scanner(System.in);

    public void mostrarMenu (){
        var json = consumoApi.obtenDatos(URL_BASE);
        System.out.println( "json=" + json);
        var datos = deserializar.obtenerDatos(json, DatosResultado.class);
        System.out.println( "datos deserializados=" + datos);

        //TOP 10 DE LISTAS DE LIBROS MAS DESCARGADOS
        System.out.println("Top 10 libros mas descargados");
        datos.resultado()
                .stream()
                .sorted(Comparator.comparing(DatosLibro::cantidadDescargas)
                .reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);
        /*
        stream = inicia el flujo de datos de la lista resultado(), lo que permite aplicar operaciones en secuencia,
        también conocidas como operaciones en cascada.
        sorted = ordena los datos por defecto de menor a mayor
        comparator.comparing = es un  metod estatico de la interfaz Comparator que simplifica la creacion de Comparadores
        basicamente compara los datos para que sea util necesita de un metodo como .sorted .sort o .max .min
        DatosLibros::cantidadDescarga = es una referencia de metodo simplificando escribir una funcion lambda que podria ser asi:
        libros -> libros.cantidadDescarga()
        Comparator.comparing(DatosLibro::cantidadDescargas) = contruye un comparador que por cada objeto de datos libros
        obtiene el valor de cantidad descarga y los compara entre si como una cadena de bicicleta girando hasta recorre
        todo los elementos de el arrays datos libro
        reversed = cambiar el orden de si antes era de menor a mayor ahora es de meyor a menor
        limit = toma los primeros 10 elementos del flujo después de que han sido ordenados.
        map = aplicamos una funcion para cada elemento de la list de datos libros y transforma cada uno de ellos a otro
        en este caso tomamos los titulos y los transformamos en mayusculas con una funcion lambda
        foreach= finalizamos el proceso de datos y los mostramos
        System.out::println= es una referencia de metodo una forma compacta de esto
        forEach(elemento -> System.out.println(elemento));
         */

        //busca de libros por nombre
        System.out.println("Ingrese el nombre del libro del que desea saber sus datos");
        String tituloLibro = leer.nextLine();

        json = consumoApi.obtenDatos(URL_BASE + BUSCAR + tituloLibro.replace(" ", "+"));
        /*
        json = es una varible que contendra los datos de respuesta de la api en formato json
        consumoApi.obtenDatos = Llama al metodo obtenDatos de un objeto consumoApi pasndo la URL completa como argumento. Este método realiza una solicitud HTTP a la API y devuelve los datos en formato JSON
        URL_BASE + BUSCAR + tituloLibro = forma una url que busca por titulo del libro
        .replace(" ","+") = si el usuario ingresa un titulo con espacios este lo remplaza con un signo mas para que no haya
        problemas a la hora de hacer la consulta a la api
         */

        var datosBusqueda = deserializar.obtenerDatos(json, DatosResultado.class);
        /*
        datosBusqueda = es una variable de tipo DatosResultados que almacena el resultado de la conversion de json a objeto
        deserializar.obtenerDatos = Llama a un metodo obtenerDatos de un objeto o clase llamada deserializar.
        Este metodo se encarga de deserializar (convertir) los datos en formato JSON que recibe como argumento.
        json = Es la cadena JSON que has obtenido previamente de la API. Contiene la respuesta de la búsqueda que has realizado.
        DatosResultado.class = Este es un argumento que indica a obtenerDatos el tipo de objeto que debe crear a partir de los datos JSON.
        En este caso, el metodo deserializará el JSON y lo convertirá en una instancia de la clase DatosResultado
         */

        Optional<DatosLibro> libroBusqueda = datosBusqueda.resultado()
                                            .stream()
                                            .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                                            .findFirst();

        if(libroBusqueda.isPresent()){
            System.out.println("Libro encontrado: " + "\n" + libroBusqueda.get());
        }else {
            System.out.println("Libro no encontrado");
        }
        /*
        esta es otra forma de hacerlo con lambdas utilizando ifpresentorelse
        libroBuscado.ifPresentOrElse(
        libro -> System.out.println("Libro encontrado: " + "\n" + libro), // Acción si hay un valor
        () -> System.out.println("Libro no encontrado"));                 // Acción alternativa si está vacío
        el Optional<DatosLibros> libroBuscado contiene el resultado opcional de la búsqueda, que puede tener un valor (es decir, un libro que coincida con la búsqueda) o estar vacío (si no se encontró ningún libro).

        Aquí está el proceso paso a paso:

        El ifPresentOrElse verifica si hay un valor dentro de libroBuscado:

        Si hay un valor presente (es decir, el libro fue encontrado), ejecuta el primer bloque, libro -> System.out.println("Libro encontrado: " + "\n" + libro), donde libro es el argumento que representa el objeto encontrado dentro del Optional. No necesitas llamar a .get() porque libro ya representa el valor contenido en Optional.
        Si no hay valor (el Optional está vacío), ejecuta el segundo bloque () -> System.out.println("Libro no encontrado"), mostrando un mensaje alternativo.
        libro ->: Este es un argumento lambda que representa el valor contenido en Optional (en este caso, el libro encontrado). No necesita .get() porque ya recibe directamente el valor.

        () ->: Este es el bloque alternativo que se ejecuta cuando Optional está vacío. El () indica que no recibe argumentos porque no hay ningún valor presente en Optional.

        Con ifPresentOrElse, puedes manejar los casos en los que Optional contiene o no contiene un valor sin preocuparte por llamar manualmente a isPresent() o .get(), y sin riesgo de un NullPointerException.
        */
        /*
        datosBusqueda.resultado() = este metodo devuelve una Lista de objetos DatosLibro
        stream = convierte la lista en un flujo stream que permite realizar operaciones con metods intermedios y finales
        filter = recibe un elemento stream y devuelve true o false
        lo que esta dentro de filter = l representa un libro en el flujo verifica si el titulo contiene el libro que estamos buscando
        findfirst = busca el primer elemento en el flujo que cumpla la condicion devuelve un optional
        obtional<datoslibro> = es para manejar un error null pointer es un contenedor que puede o no estar vacio
        isPresent = este es un metodo de optional que verifica si el contenido esta vacio o no y devuelve true si contiene valor y false si no
         */

        //Trabajo con estadisticas
        DoubleSummaryStatistics est = datos.resultado()
                                      .stream()
                                      .filter(d -> d.cantidadDescargas() > 0)
                                      .collect(Collectors.summarizingDouble(DatosLibro::cantidadDescargas));

        /*
        est = variable doublesummarystatistics donde almacenaremos la cantidadDescarga ya procesados
        datos.resultado()= trae la lista de datos libros que a su vez trae lista de datos autor
        stream= para iniciar el proceso de flujo de datos y aplicar sus métodos intermedios y finales
        filter= filtra reduce el conjunto de datos a solo aquellos que interesan en este caso los datos de cantidadDescarga que sean mayor a 0
        sin necesidad de recorrer y evaluar los elementos uno a uno en un bucle tradicional
        collect= finaliza el proceso de flujo de datos y transforma los datos en una colección o un tipo de datos específico
        colección= List, Set, Map
        tipo de datos especifico = como Collectors.joining(", ") que puede tomar los datos de una stream y los concatena en un solo String
        o en este caso:
        Collectors.summarizingDouble(...): Es un colector que analiza numéricamente los elementos del Stream, produciendo un objeto de tipo DoubleSummaryStatistics. Este colector permite resumir estadísticas, como el promedio, el máximo, el mínimo y el conteo de elementos.
        DatosLibro::cantidadDescargas: Es una referencia de método que señala qué atributo se debe analizar en cada elemento del Stream. Aquí, se está refiriendo al método cantidadDescargas de la clase DatosLibro, que devuelve el número de descargas de cada libro.


        doublesummarystatistics = es una clase que sirve para recopilar estadísticas de valores double de manera eficiente en una sola operacion proporciona
        calcular automaticamente valores como:
        suma(getSuma())= la suma de todos los elementos procesados en este caso la cantidad de descarga de la lista de libros
        contador(getCount())= la cantidad de elementos en el flujo
        media (getAverage())= el promedio de los elementos
        mínimo (getMin())= el valor mínimo
        máximo (getMax())= el valor máximo
        los métodos que podemos aplicar son:
        .mapToDouble()= convierte los elementos a double en este caso no lo necesitamos por que ya teniamos los datos de cantidadDescarga en double
        .summaryStatistics()= calcula todas las estadísticas en un solo paso y luefo lo podemos llamar con sus métodos get
         */


    }



}
