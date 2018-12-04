# multichat

<h3> TCP </h3>
Programa de chat creado para ver la interacción de los protocolos TCP y UDP y los grupos de difusión.
Para que funcione una conexión TCP primero debe indicarse el puerto de escucha y luego hacer click en 
el botón TCP para esperar por una conexión.

En otra instancia del programa un cliente que se vaya a conectar al servidor debe indicar la dirección
ip del mismo y el puerto de escucha (que en este caso es el puerto al que se conectará en el servidor),
y luego hacer click en conectar.

El tiempo de espera para la conexión es de **50 seg**. Una vez que se ha establecido la conexión se
mostrara una nueva entrada en el area de texto superior izquierda.

Para enviar un mensaje se debe seleccionar el destinatario de la lista antes mencionada. Se pueden establecer
más conexiones pero no se pueden utilizar los mismos puertos para el servidor.

Para terminar una conexión la seleccionamos de la lista de conectados y hacemos click en el botón desconectar.
