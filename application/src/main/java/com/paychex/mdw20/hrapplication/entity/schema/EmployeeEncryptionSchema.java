/*
 * # Copyright 2020 Paychex, Inc.
 * # Licensed pursuant to the terms of the Apache License, Version 2.0 (the "License");
 * # your use of the Work is subject to the terms and conditions of the License.
 * # You may obtain a copy of the License at
 * #
 * # http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Disclaimer of Warranty. Unless required by applicable law or agreed to in writing, Licensor
 * # provides the Work (and each Contributor provides its Contributions) on an "AS IS" BASIS,
 * # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including,
 * # without limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT,
 * # MERCHANTABILITY, OR FITNESS FOR A PARTICULAR PURPOSE. You are solely responsible
 * # for determining the appropriateness of using or redistributing the Work and assume
 * # any risks associated with your exercise of permissions under this License.
 */

package com.paychex.mdw20.hrapplication.entity.schema;

import java.util.Arrays;
import org.bson.Document;

public class EmployeeEncryptionSchema {

	public static Document getDocument(String keyId) {

		Document document = new Document();
		document.append("bsonType", "object").append("properties",
				new Document().append("ssn", buildEncryptedField("int", true, keyId)).append("bloodType",
						buildEncryptedField("string", false, keyId)).append("phone",
						buildEncryptedField("string", false, keyId)).append("salary",
						buildEncryptedField("double", false, keyId)));

		return document;
	}

	// JSON Schema helpers
	private static Document buildEncryptedField(String bsonType, Boolean isDeterministic, String keyId) {
		String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
		String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";

		return new Document().
				append("encrypt", new Document().append("bsonType", bsonType).append("keyId", Arrays.asList(
						new Document().append("$binary",
								new Document().append("base64", keyId).append("subType", "04")))).append("algorithm",
						(isDeterministic) ? DETERMINISTIC_ENCRYPTION_TYPE : RANDOM_ENCRYPTION_TYPE));
	}

}
