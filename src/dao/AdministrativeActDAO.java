package dao;

import models.AdministrativeAct;
import utilities.DbTable;

import java.sql.ResultSet;
import java.util.List;

public class AdministrativeActDAO extends BaseDAO<AdministrativeAct> {
    public AdministrativeActDAO() {
        super(DbTable.ACT.getName());
    }

    @Override
    protected List<AdministrativeAct> getEntitiesByResultSet(ResultSet resultSet) {
        return List.of();
    }
}
