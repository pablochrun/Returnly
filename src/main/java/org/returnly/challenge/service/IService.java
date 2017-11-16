package org.returnly.challenge.service;

import java.util.List;

import org.returnly.challenge.entity.Order;

import com.fasterxml.jackson.databind.JsonNode;

public interface IService {

	public JsonNode getJSONNodeFromConnection(String method, String path);
	public List<Order> getListOrderFromConnection(String method, String path, int limit, int page);
}
