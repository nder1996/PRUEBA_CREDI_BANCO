package crediBanco.repository;



import crediBanco.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, String> {

    /*
     @Query(value = "SELECT Roles.idRol, Roles.descripcion Rol, Personas.id 'Id Personas',Usuarios.usuario Usuario, aplicacion.idAplicacion, aplicacion.nombre Aplicacion, " +
            "CONCAT_WS(' ',TRIM(Personas.primerNombre),TRIM(IFNULL(Personas.segundoNombre, '')),TRIM(Personas.primerApellido),TRIM(IFNULL(Personas.segundoApellido, ''))) Nombre , cliente.id IDCLIENTE " +
            "FROM Roles " +
            "LEFT JOIN UsuarioRolesAplicacion AS usuarioRoles ON usuarioRoles.idRol = Roles.idRol " +
            "LEFT JOIN Aplicaciones AS aplicacion ON usuarioRoles.idAplicacion = aplicacion.idAplicacion " +
            "LEFT JOIN Usuarios ON usuarioRoles.usuario = Usuarios.usuario " +
            "LEFT JOIN Personas ON Usuarios.idPersonas = Personas.id " +
            "LEFT JOIN CotCliente as cliente on Personas.id = cliente.idPersonas " +
            "WHERE usuarioRoles.usuario = :username AND usuarioRoles.estado='A' AND Usuarios.estado='A' and aplicacion.idAplicacion=18", nativeQuery = true)
    List<Object[]> userRoles(@Param("username") String username);
    * */


}
