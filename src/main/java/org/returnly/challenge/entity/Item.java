package org.returnly.challenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class that modele an Item info
 * 
 * @author pablo
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
	
	private String product_id;
	private String variant_id;
	private String title;	

	public String getVariant_id() {
		return variant_id;
	}
	public void setVariant_id(String variant_id) {
		this.variant_id = variant_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}	
	
	public String toString(){
		return "Product_Id: "+this.product_id + ", title: "+ this.title + ", variant: "+ this.variant_id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product_id == null) ? 0 : product_id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((variant_id == null) ? 0 : variant_id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (product_id == null) {
			if (other.product_id != null)
				return false;
		} else if (!product_id.equals(other.product_id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (variant_id == null) {
			if (other.variant_id != null)
				return false;
		} else if (!variant_id.equals(other.variant_id))
			return false;
		return true;
	}
}