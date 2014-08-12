package com.maxpowa.chime.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxpowa.chime.Chime;

public class UpdateChecker {

	/*
{
  "title": "Chime",
  "game": "Minecraft",
  "category": "Cosmetic",
  "url": "http:\/\/www.curse.com\/mc-mods\/minecraft\/223265-chime",
  "thumbnail": "http:\/\/media-curse.cursecdn.com\/attachments\/124\/227\/8547408bf1bc3206e02abd7f621dbeb2.png",
  "authors": [
    "Maxpowa"
  ],
  "downloads": {
    "monthly": 4,
    "total": 4
  },
  "favorites": 0,
  "likes": 0,
  "updated_at": "2014-08-11T18:10:58+0000",
  "created_at": "2014-08-11T16:19:48+0000",
  "project_url": "http:\/\/www.curseforge.com\/projects\/223265\/",
  "release_type": "Beta",
  "license": "Custom License",
  "download": {
    "id": 2211322,
    "url": "http:\/\/curse.com\/mc-mods\/minecraft\/223265-chime\/2211322",
    "name": "Chime-1.7.10-0.1.178.jar",
    "type": "beta",
    "version": "1.7.10",
    "downloads": 3,
    "created_at": "2014-08-11T16:22:48+0000"
  },
  "versions": {
    "1.7.10": [
      {
        "id": 2211322,
        "url": "http:\/\/curse.com\/mc-mods\/minecraft\/223265-chime\/2211322",
        "name": "Chime-1.7.10-0.1.178.jar",
        "type": "beta",
        "version": "1.7.10",
        "downloads": 3,
        "created_at": "2014-08-11T16:22:48+0000"
      }
    ]
  },
  "files": {
    "2211322": {
      "id": 2211322,
      "url": "http:\/\/curse.com\/mc-mods\/minecraft\/223265-chime\/2211322",
      "name": "Chime-1.7.10-0.1.178.jar",
      "type": "beta",
      "version": "1.7.10",
      "downloads": 3,
      "created_at": "2014-08-11T16:22:48+0000"
    }
  }
}
	 */

	public static ChimeVersion latest = null;
	public static boolean dismissed = false;
	static ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
	}

	public static void checkUpdate() {
		try {
			URLConnection versionData = new URL("http://widget.mcf.li/mc-mods/minecraft/223265-chime.json").openConnection();
			JsonNode object = mapper.readTree(new BufferedReader(new InputStreamReader(versionData.getInputStream())));
			//Utils.log.info(object.toString());
			if (object.get("versions").has(Chime.MC_VERSION)) {
				latest = mapper.treeToValue(object.get("versions").get(Chime.MC_VERSION).get(0), ChimeVersion.class);
				//Utils.log.info(object.get("versions").get(Chime.MC_VERSION).get(0).toString());
			} else {
				Utils.log.info("Update checker was unable to find a release for this Minecraft version, showing latest release.");
				//Utils.log.info(object.get("download").toString());
				latest = mapper.treeToValue(object.get("download"), ChimeVersion.class);
			}
		} catch (IOException e) {
			Utils.log.error("Update checker failed to check for new version.",e);
		}
	}
}
