package dao;

import models.Demand;
import utilities.DbTable;

import java.sql.ResultSet;
import java.util.List;

public class DemandDAO extends BaseDAO<Demand> {
    public DemandDAO() {super(DbTable.DEMAND.getName());}

    @Override
    protected List<Demand> findEntitiesByResultSet(ResultSet resultSet) {
        return List.of();
    }
}
