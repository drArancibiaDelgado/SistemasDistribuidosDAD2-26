create database Terminal {
    create table Pasajero{
        primary key CI varchar(8);
        Nombre varchar(50);
        Apellidos varchar(50);
        Celular int(8);
        Frecuente boolean();    
    }
}