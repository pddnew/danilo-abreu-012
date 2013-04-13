/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grafosugestao;

/**
 *
 * @author Leticia
 */
public class Produto extends Vertice{
    private String nome;
    private String categoria;
    private double valor;

    
    public Produto(String nome, String categoria, double valor) {
        this.nome = nome;
        this.categoria = categoria;
        this.valor = valor;
    }
 
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        hash = 89 * hash + (this.categoria != null ? this.categoria.hashCode() : 0);
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.valor) ^ (Double.doubleToLongBits(this.valor) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Produto other = (Produto) obj;
        if ((this.nome == null) ? (other.nome != null) : !this.nome.equals(other.nome)) {
            return false;
        }
        if ((this.categoria == null) ? (other.categoria != null) : !this.categoria.equals(other.categoria)) {
            return false;
        }
        if (Double.doubleToLongBits(this.valor) != Double.doubleToLongBits(other.valor)) {
            return false;
        }
        return true;
    }

    

   

    @Override
    public String toString() {
        return nome + "Categoria: " + categoria + "Valor= " + valor;
    }
    
}
