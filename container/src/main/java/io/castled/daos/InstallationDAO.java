package io.castled.daos;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface InstallationDAO {

    @SqlQuery("select id from installations limit 1")
    String getInstallation();

    @SqlUpdate("insert into installations(id) values(:id)")
    void createInstallation(@Bind("id") String installationId);
}
