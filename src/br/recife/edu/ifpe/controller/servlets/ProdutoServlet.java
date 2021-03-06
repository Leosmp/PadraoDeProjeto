package br.recife.edu.ifpe.controller.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.recife.edu.ifpe.model.classes.ItemEstoque;
import br.recife.edu.ifpe.model.classes.Produto;
import br.recife.edu.ifpe.model.dao.DaoFactory;

/**
 * Servlet implementation class ProdutoServlet
 */
@WebServlet("/ProdutoServlet")
public class ProdutoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProdutoServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int codigo = Integer.parseInt(request.getParameter("codigo"));

		Produto p = DaoFactory.createProdutosJDBC().findById(codigo);
		request.setAttribute("produto", p);

		getServletContext().getRequestDispatcher("/produtos.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String nome = request.getParameter("nome");
		String marca = request.getParameter("marca");
		String categoria = request.getParameter("categoria");
		String descricao = request.getParameter("descricao");

		String atualiza = request.getParameter("atualizar");

		Produto p = new Produto(null, nome, marca, categoria, descricao);
		HttpSession session = request.getSession();

		if (atualiza == null) {
			
			DaoFactory.createProdutosJDBC().insert(p);
			
			ItemEstoque itemEstoque = new ItemEstoque(p,0);
			DaoFactory.createEstoqueJDBC().insert(itemEstoque); 
			
			session.setAttribute("msg", "Produto " + p.getNome() + " foi cadastrado!");
		} else {
			int codigo = Integer.parseInt(request.getParameter("codigo"));
			p.setCodigo(codigo);
			DaoFactory.createProdutosJDBC().update(p);
			session.setAttribute("msg", "Produto " + p.getNome() + " foi atualizado!");
		}

		response.sendRedirect("produtos.jsp");
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		int codigo = Integer.parseInt(req.getParameter("codigo"));
		
		Produto p = DaoFactory.createProdutosJDBC().findById(codigo);
		DaoFactory.createProdutosJDBC().delete(codigo);
		
		HttpSession session = req.getSession();
		
		session.setAttribute("msg", "Produto " + p.getNome() + " foi deletado!");
	}

}
