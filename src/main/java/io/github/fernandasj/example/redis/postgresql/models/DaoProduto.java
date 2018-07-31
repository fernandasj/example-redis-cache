package io.github.fernandasj.example.redis.postgresql.models;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import redis.clients.jedis.Jedis;

/**
 *
 * @author fernanda
 */
public class DaoProduto {
    
    ConnectionFactory connectionFactory = new ConnectionFactory();
    
    public boolean save(Produto p){
        final String sql = "INSERT INTO produto (codigo, descricao, preco) VALUES (?, ?, ?);";
        try (Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, 
                        PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, p.getCodigo());
            statement.setString(2, p.getDescricao());
            statement.setFloat(3, p.getPreco());
            statement.execute();
            statement.close();
            saveRedis(p);
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean saveRedis(Produto p){
        Jedis jedis = new Jedis("localhost", 6379);
        Gson gson = new Gson();
        String json = gson.toJson(p);
        jedis.setex("" + p.getCodigo(), 1800, json);
        System.out.println(jedis.get("" + p.getCodigo()));
        jedis.close();
        return true;
    }
    
    public Produto search (int codigo){
        Produto produto = null;
        final String sql = "SELECT * FROM produto p WHERE p.codigo="+codigo;
        try (Connection connection = connectionFactory.getConnection();
                Statement statement = connection.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE, 
                    ResultSet.CONCUR_READ_ONLY, 
                    ResultSet.CLOSE_CURSORS_AT_COMMIT
                );) {
            
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                produto = new Produto();
                produto.setCodigo(result.getInt("codigo"));
                produto.setDescricao(result.getString("descricao"));
                produto.setPreco(result.getFloat("preco"));
                                
                return produto;
            }
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return produto;
    }
    
    public Produto validateProduto(int codigo){
        DaoProduto daoProduto = new DaoProduto();
        Produto produto = null;
        Jedis jedis = new Jedis("localhost", 6379);
        Gson gson = new Gson();
        String result = jedis.get(""+codigo); 
        
        if(result != null){
            JOptionPane.showMessageDialog(null, "Produto no Redis");
            Produto p = gson.fromJson(result, Produto.class);
            produto = new Produto(p.getCodigo(), p.getDescricao(), p.getPreco());
            System.out.println(produto);
        } else {
            produto = daoProduto.search(codigo);
            if(produto == null){
                JOptionPane.showMessageDialog(null, "Produto não encontrado");
                return null;
            } else {
                JOptionPane.showMessageDialog(null, "Produto no Postgres");
                saveRedis(produto);
            }
        jedis.close();
        }
        return produto;
    }
}
