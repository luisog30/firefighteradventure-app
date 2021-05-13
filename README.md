

App:    
-> Obligar a que las contraseñas introducidas tengan un cierto formato (por ejemplo mínimo 1 mayúscula y 1 minúscula, 1 carácter especial, 1 número...)
-> Si se añade el campo "email" al register o al login, añadir un sistema mediante el cual se compruebe que el formato del email introducido es el correcto (básicamente para que no se pueda escribir algo que no sea un email)
-> Vista de admin



Web:
-> Vista para usuario modificar cualquiera de sus datos
-> Vista para el admin para poder modificar cualquier dato de cualquier usuario
-> Poner tabla para ver el ranking de Puntuación 
-> Vista para ver cesta de productos del usuario?
-> Vista ("Shop done!")?

Backend Añadir:
-> Clase Enemigo ( Enemigo1, Enemigo2, Enemigo3)                                            (Enemy)

-> Clase Objeto ( Objeto1,... )                                                             (Item, referintse al inventari) 
                                                                                            però també he de afegir objecte (referint-se a objectes del mapa?)
                                                                                            
-> Clase Arma ( Arma1,... )                                                                Armas-> No estàn a la bbdd encara, pero poden comptar com a items, no?

-> Sesiones(Login, Logout)                                                                    No se com implementar que es mantingui la sesió oberta


Part Botiga(Backend): 
- Clase Cistella (Lista)        
- Clase productos (Hashmap?)                                                                  (Es diu Elements a Backend)
- Clase Orders (Cola) (SELECT Orders From User WHERE Username....)o millor amb ID?    
                                                                                             (està feta la consulta de les dues maneres, però es millor sempre buscar amb el ID,                                                                                               perque les relacions a les taules de bbdd estàn fetes aamb els ID...)
                                                                                             No està com a cua ENCARA...

Màquina
- Exportar BBDD DESDE la máquina a IntellIJ
