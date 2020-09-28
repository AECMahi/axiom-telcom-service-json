package com.axiom.search.handset.filtering;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@RestController
public class SearchController {
	static Set<String> listReturn = null;

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> retrieveListOfHandsets(@RequestParam Map<String, String> searchParams) {

		Set<String> setOfJSONObjects = new HashSet<String>();
		JSONArray jsonArray = new JSONArray(
				new JSONTokener(SearchController.class.getResourceAsStream("/handset.json")));
		System.out.println("JSON Object :" + jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json2 = jsonArray.getJSONObject(i);
			Set<String> returnSet = null;
			JsonParser p = new JsonParser();
			if (searchParams != null && searchParams.size() == 1) {
				returnSet = checkSingle(searchParams, p.parse(json2.toString()), false);
			} else {
				returnSet = checkMultiSearch(searchParams, p.parse(json2.toString()), false);
			}
			if (null != returnSet) {
				// System.out.println("JSON: " + json2);
				if (!setOfJSONObjects.contains(json2.toString()))
					setOfJSONObjects.add(json2.toString());
			}
		}

		System.out.println("setOfJSONObjects size: " + setOfJSONObjects.size());
		System.out.println(setOfJSONObjects);
		Map<String, String> returnMap = new LinkedHashMap<>();
		returnMap.put("Count Of Devices :: ", String.valueOf(setOfJSONObjects.size()));
		returnMap.put("Return Devices Details(JSON) ::--> ", setOfJSONObjects.toString());

		return new ResponseEntity<>(returnMap, HttpStatus.OK);
	}

	private static Set<String> checkSingle(Map<String, String> searchParams, JsonElement jsonElement, boolean isFound) {

		if (!isFound)
			listReturn = null;
		if (jsonElement.isJsonArray()) {
			for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
				checkSingle(searchParams, jsonElement1, false);
			}
		} else {
			if (jsonElement.isJsonObject()) {
				Set<Map.Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();

				Set<String> jsonObjectKeyset = jsonElement.getAsJsonObject().keySet();
				Map<String, Boolean> areEqualKeyValues = entrySet.stream().collect(
						Collectors.toMap(e -> e.getKey(), e -> e.getValue().equals(searchParams.get(e.getKey()))));
				if (jsonObjectKeyset.containsAll(searchParams.keySet())) {
					// System.out.println("All searchParams present in this JSON" +
					// searchParams.keySet().toString());

					if (!(areEqualKeyValues.isEmpty())) {

						for (Map.Entry<String, JsonElement> entry : entrySet) {
							String key1 = entry.getKey();

							Iterator<String> itr = searchParams.keySet().iterator();
							while (itr.hasNext()) {
								if (areEqualKeyValues.containsKey(key1)) {
									String key = itr.next();

									if (key.equals(key1)) {
										String value = searchParams.get(key);

										isFound = entry.getValue().getAsString().toLowerCase()
												.indexOf(value.toLowerCase()) != -1 ? true : false; // true
										if (isFound) {
											// System.out.println(key + "=" + value);
											listReturn = new HashSet<String>();
											listReturn.add("Present");
										}

									}
								}
								break;
								// System.out.println("Actual Present:: "+);
							}
						}
					}

				} else {
					for (Map.Entry<String, JsonElement> entry : entrySet) {
						String key1 = entry.getKey();
						if (areEqualKeyValues.containsKey(key1) && areEqualKeyValues.get(key1)) {
							isFound = entry.getValue().getAsString().indexOf(searchParams.get(key1)) != -1 ? true
									: false; // true
							if (isFound) {
								listReturn = new HashSet<String>();
								listReturn.add(entry.getValue().toString());
								System.out.println("Actual :: " + entry.getValue().toString());
								break;
							}
						}
						checkSingle(searchParams, entry.getValue(), true);
					}

				}
			}
		}
		// System.out.println("Return size " + listReturn.size());
		return listReturn;

	}

	private static Set<String> checkMultiSearch(Map<String, String> searchParams, JsonElement jsonElement,
			boolean isFound) {
		if (!isFound)
			listReturn = null;
		if (jsonElement.isJsonArray()) {
			for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
				checkMultiSearch(searchParams, jsonElement1, false);
			}
		} else {
			if (jsonElement.isJsonObject()) {
				Set<Map.Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();

				Set<String> jsonObjectKeyset = jsonElement.getAsJsonObject().keySet();
				Map<String, Boolean> areEqualKeyValues = entrySet.stream().collect(
						Collectors.toMap(e -> e.getKey(), e -> e.getValue().equals(searchParams.get(e.getKey()))));
				if (jsonObjectKeyset.containsAll(searchParams.keySet())) {
					// System.out.println("All searchParams present in this JSON" +
					// searchParams.keySet().toString());

					if (!(areEqualKeyValues.isEmpty())) {
						listReturn = new HashSet<String>();
						listReturn.add("Present");
						// System.out.println("Actual Present:: ");
					}

				} else {
					for (Map.Entry<String, JsonElement> entry : entrySet) {
						String key1 = entry.getKey();
						if (areEqualKeyValues.containsKey(key1) && areEqualKeyValues.get(key1)) {
							isFound = entry.getValue().getAsString().indexOf(searchParams.get(key1)) != -1 ? true
									: false; // true
							if (isFound) {
								listReturn = new HashSet<String>();
								listReturn.add(entry.getValue().toString());
								// System.out.println("Actual :: "+entry.getValue().toString());
								break;
							}
						}
						checkMultiSearch(searchParams, entry.getValue(), true);
					}

				}
			}
		}
		// System.out.println("Return size " + listReturn.size());
		return listReturn;
	}

}
