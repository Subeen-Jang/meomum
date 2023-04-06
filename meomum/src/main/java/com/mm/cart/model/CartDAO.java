package com.mm.cart.model;
import java.util.*;


public interface CartDAO {

	public int cartInsert(CartDTO dto);
	public int cartCheck(int pro_idx);
	public List<CartDTO> cartList(int user_idx);
	public int cartDelete(int cart_idx);
	public int cartNumUpdate(CartDTO dto);
}