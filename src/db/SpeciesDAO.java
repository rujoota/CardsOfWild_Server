package db;

// Java Imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Other Imports
import core.ServerResources;
import model.AnimalType;
import model.PlantType;
import model.SpeciesType;
import model.Consume;

import util.Log;

/**
 * Table(s) Required: species, species_nodes, consume
 *
 * @author Gary
 */
public final class SpeciesDAO {

    private SpeciesDAO() {
    }

    public static List<SpeciesType> getSpecies() {
        List<SpeciesType> types = new ArrayList<SpeciesType>();

        String query = ""
                + "SELECT *, "
                + "GROUP_CONCAT(`node_id`, ':', `distribution`) AS node_list, "
                + "(SELECT GROUP_CONCAT(`prey_id`) FROM `consume` WHERE `species_id` = s.`species_id`) AS prey_list, "
                + "(SELECT GROUP_CONCAT(`species_id`) FROM `consume` WHERE `prey_id` = s.`species_id`) AS predator_list "
                + "FROM `species` s "
                + "INNER JOIN `species_nodes` sn ON s.`species_id` = sn.`species_id` "
                + "GROUP BY s.`species_id` "
                + "ORDER BY s.`species_id`, sn.`node_id`";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = GameDB.getConnection();
            pstmt = con.prepareStatement(query);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                SpeciesType type = null;

                switch (rs.getInt("organism_type")) {
                    case 0:
                        type = new AnimalType(rs.getInt("species_id"));
                        break;
                    case 1:
                        type = new PlantType(rs.getInt("species_id"));
                        type.setCarryingCapacity(rs.getFloat("carrying_capacity"));
                        break;
                }

                if (type == null) {
                    continue;
                }

                type.setName(rs.getString("name"));
                type.setOrganismType(rs.getInt("organism_type"));
                type.setCost(rs.getInt("cost"));
                type.setDescription(rs.getString("description"));
                type.setCategory(rs.getString("category"));
                type.setBiomass(rs.getInt("biomass"));
                type.setDietType(rs.getShort("diet_type"));
                type.setMetabolism(rs.getFloat("metabolism"));
                type.setTrophicLevel(rs.getFloat("trophic_level"));
                type.setGrowthRate(rs.getFloat("growth_rate"));
                type.setModelID(rs.getInt("model_id"));

                // Node Distribution
                Map<Integer, Float> nodeDistribution = new HashMap<Integer, Float>();
                String[] nodeList = rs.getString("node_list").split(",");
                for (String node : nodeList) {
                    String[] pair = node.split(":");
                    nodeDistribution.put(Integer.parseInt(pair[0]), Float.parseFloat(pair[1]));
                }
                type.setNodeDistribution(nodeDistribution);

                // Prey List
                String[] preyStr = rs.getString("prey_list") == null ? new String[0] : rs.getString("prey_list").split(",");
                int[] preyList = new int[preyStr.length];
                for (int i = 0; i < preyStr.length; i++) {
                    preyList[i] = Integer.parseInt(preyStr[i]);
                }
                type.setPreyIDs(preyList);

                // Predator List
                String[] predatorStr = rs.getString("predator_list") == null ? new String[0] : rs.getString("predator_list").split(",");
                int[] predatorList = new int[predatorStr.length];
                for (int i = 0; i < predatorStr.length; i++) {
                    predatorList[i] = Integer.parseInt(predatorStr[i]);
                }
                type.setPredatorIDs(predatorList);

                types.add(type);
            }
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        } finally {
            GameDB.closeConnection(con, pstmt, rs);
        }

        return types;
    }

    

    /**
     * Get list of species IDs for species fitting specified "WHERE" clause.
     * Used only in Sim_Job environment. 4/21/14, JTC
     *
     * 9/13/14 - JTC - changed SQL stmt to base inclusion on eco_type rather than
     * field "hidden" (functionality replaced by Gary Ng).  Added argument "eco_type".

* @param whereClause
     * @param eco_type
     * @return List<Integer> species IDs
     */
    public static List<Integer> getSpeciesIdList(String whereClause, int eco_type) {
        List<Integer> speciesIdList = new ArrayList<Integer>();
        
        String query = ""
                + "SELECT s.`species_id` FROM `species` s "
                + "JOIN `eco_type_species` ets ON s.`species_id` = ets.`species_id` "
                + "WHERE ets.`eco_type` = " + eco_type + " "
                + (whereClause.isEmpty() ? "" : "AND " + whereClause + " ")
                + "ORDER BY s.`species_id`";

        /*
        String query = ""
                + "SELECT `species_id` FROM `species` WHERE `hidden` = 0 "
                + (whereClause.isEmpty() ? "" : "AND " + whereClause + " ")
                + "ORDER BY `species_id`";
        */
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = GameDB.getConnection();
            pstmt = con.prepareStatement(query);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                speciesIdList.add(rs.getInt("species_id"));
            }
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        } finally {
            GameDB.closeConnection(con, pstmt, rs);
        }

        return speciesIdList;
    }
}
