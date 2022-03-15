package infra;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class DAO<E> {

	private static EntityManagerFactory emf;
	private EntityManager em;
	private Class<E> classe;

	static {
		try {
			emf = Persistence.createEntityManagerFactory("exercicios-jpa");
		} catch (Exception e) {
			// Logar
		}
	}

	public DAO(Class<E> classe) {
		this.classe = classe;
		em = emf.createEntityManager();
	}

	public DAO() {
		this(null);
	}

	// Abrir Transa��o
	public DAO<E> abrirT() {
		em.getTransaction().begin();
		return this;
	}

	// Fechar Transa��o
	public DAO<E> fecharT() {
		em.getTransaction().commit();
		return this;
	}

	// Incluir Entidade
	public DAO<E> incluir(E entidade) {
		em.persist(entidade);
		return this;
	}

	// Incluir At�mico/Completo (Inclus�o com transa��o - Abertura e Fechamento)
	public DAO<E> incluirAtomico(E entidade) {
		return this.abrirT().incluir(entidade).fecharT();
	}

	// Obter todos sempre limitado a 10
	public List<E> obterTodos() {
		return this.obterTodos(10, 0);
	}

	// Obter todos (Consulta com limit e deslocamento)
	public List<E> obterTodos(int qtde, int deslocamente) {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe nula!!");
		}

		String jpql = "select e from " + classe.getName() + " e";
		TypedQuery<E> query = em.createQuery(jpql, classe);

		query.setMaxResults(qtde);
		query.setFirstResult(deslocamente);
		return query.getResultList();
	}

	// Fechar conex�o
	public void fechar() {
		em.close();
	}

}