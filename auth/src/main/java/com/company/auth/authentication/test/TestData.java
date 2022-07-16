package com.company.auth.authentication.test;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Data;
import lombok.experimental.Accessors;

public class TestData {
	@Data
	@Accessors(chain = true)
	public static class UserInfo {
		private Integer id;
		private String nickname;
		private String avator;
	}

	@Data
	@Accessors(chain = true)
	public static class Oauth {
		private Integer userId;
		private String identityType;
		private String identifier;
		private String certificate;
	}
	
	public static List<UserInfo> userInfoList = Lists.newArrayList();
	static {
		userInfoList.add(new UserInfo().setId(83848).setNickname("candi").setAvator(
				"https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132"));
	}

	public static List<Oauth> oauthList = Lists.newArrayList();
	static {
		oauthList.add(new Oauth().setUserId(83848).setIdentityType("username").setIdentifier("15220163215")
				.setCertificate("12345678"));
		oauthList.add(new Oauth().setUserId(83848).setIdentityType("mobile").setIdentifier("15220163215")
				.setCertificate("123456"));
	}

	public static Map<String, String> redis = Maps.newHashMap();
	
	public static UserInfo registerUser(String nickname, String avator) {
		int maxId = userInfoList.stream().mapToInt(UserInfo::getId).max().getAsInt();
		UserInfo userInfo = new UserInfo().setId(maxId + 1).setNickname(nickname).setAvator(avator);
		userInfoList.add(userInfo);
		return userInfo;
	}

	public static void bindOauth(Integer userId, String identityType, String identifier, String certificate) {
		Oauth oauthDB = oauthList.stream()
				.filter(a -> a.getUserId().equals(userId) && a.getIdentityType().equals(identityType)).findFirst()
				.orElse(null);
		if (oauthDB == null) {
			Oauth oauth = new Oauth().setUserId(userId).setIdentityType(identityType).setIdentifier(identifier)
					.setCertificate(certificate);
			oauthList.add(oauth);
		} else {
			oauthDB.setIdentifier(identifier).setCertificate(certificate);
		}
	}

	public static Oauth selectOauth(String identifier, String identityType) {
		return oauthList.stream()
				.filter(a -> a.getIdentifier().equals(identifier) && a.getIdentityType().equals(identityType))
				.findFirst().orElse(null);
	}
	
	public static Map<String, String> codeOpenidMap = Maps.newHashMap();
	static {
		codeOpenidMap.put("1001", "{\"access_token\":\"58_hutKyhcT5biJsn04NZ9jHW9GZVgCVOF4YVlK3NZiivKq8rJkWs2-la-nhJNhuisNs-fwFtfeaIyOuWNYC9AOW9fzv3oz_pMcg_zUYoZplZc1\",\"expires_in\":7200,\"refresh_token\":\"58_luzGuma7ZudDzx4kF1-YCo9wNUI8D9-FWDLCtGg6idqwBy7yJ2ssAMpjgNR-uQz603Mrm2gadX5B1XRHZ76gEMooN-T2Fn514skqOWTOWMM1\",\"openid\":\"oYB7c6mHjNn4LW_zXXPsDR8mVSJ1\",\"scope\":\"snsapi_userinfo\",\"unionid\":\"oiPIJuEo0OzxLqzSEWZYZ-nVWmT1\"}");
		codeOpenidMap.put("1002", "{\"access_token\":\"58_hutKyhcT5biJsn04NZ9jHW9GZVgCVOF4YVlK3NZiivKq8rJkWs2-la-nhJNhuisNs-fwFtfeaIyOuWNYC9AOW9fzv3oz_pMcg_zUYoZplZc2\",\"expires_in\":7200,\"refresh_token\":\"58_luzGuma7ZudDzx4kF1-YCo9wNUI8D9-FWDLCtGg6idqwBy7yJ2ssAMpjgNR-uQz603Mrm2gadX5B1XRHZ76gEMooN-T2Fn514skqOWTOWMM2\",\"openid\":\"oYB7c6mHjNn4LW_zXXPsDR8mVSJ2\",\"scope\":\"snsapi_userinfo\",\"unionid\":\"oiPIJuEo0OzxLqzSEWZYZ-nVWmT2\"}");
		codeOpenidMap.put("1003", "{\"access_token\":\"58_hutKyhcT5biJsn04NZ9jHW9GZVgCVOF4YVlK3NZiivKq8rJkWs2-la-nhJNhuisNs-fwFtfeaIyOuWNYC9AOW9fzv3oz_pMcg_zUYoZplZc3\",\"expires_in\":7200,\"refresh_token\":\"58_luzGuma7ZudDzx4kF1-YCo9wNUI8D9-FWDLCtGg6idqwBy7yJ2ssAMpjgNR-uQz603Mrm2gadX5B1XRHZ76gEMooN-T2Fn514skqOWTOWMM3\",\"openid\":\"oYB7c6mHjNn4LW_zXXPsDR8mVSJ3\",\"scope\":\"snsapi_userinfo\",\"unionid\":\"oiPIJuEo0OzxLqzSEWZYZ-nVWmT3\"}");
	}
	
	public static Map<String, String> openidInfoMap = Maps.newHashMap();
	static {
		openidInfoMap.put("oYB7c6mHjNn4LW_zXXPsDR8mVSJ1", "{\"openid\":\"oYB7c6mHjNn4LW_zXXPsDR8mVSJ1\",\"nickname\":\"勝1\",\"sex\":0,\"language\":\"\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"headimgurl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/4gV1O4eXicKuKlmuG5aniaNadRm3hkbbdp5qRd8MMY946ia1eDiah2ttfFeLRp8CEn5HFda6AKCM67GWbDj11dzy1Q/132\",\"privilege\":[],\"unionid\":\"oiPIJuEo0OzxLqzSEWZYZ-nVWmT1\"}");
		openidInfoMap.put("oYB7c6mHjNn4LW_zXXPsDR8mVSJ2", "{\"openid\":\"oYB7c6mHjNn4LW_zXXPsDR8mVSJ2\",\"nickname\":\"勝2\",\"sex\":0,\"language\":\"\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"headimgurl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/4gV1O4eXicKuKlmuG5aniaNadRm3hkbbdp5qRd8MMY946ia1eDiah2ttfFeLRp8CEn5HFda6AKCM67GWbDj11dzy1Q/132\",\"privilege\":[],\"unionid\":\"oiPIJuEo0OzxLqzSEWZYZ-nVWmT2\"}");
		openidInfoMap.put("oYB7c6mHjNn4LW_zXXPsDR8mVSJ3", "{\"openid\":\"oYB7c6mHjNn4LW_zXXPsDR8mVSJ3\",\"nickname\":\"勝3\",\"sex\":0,\"language\":\"\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"headimgurl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/4gV1O4eXicKuKlmuG5aniaNadRm3hkbbdp5qRd8MMY946ia1eDiah2ttfFeLRp8CEn5HFda6AKCM67GWbDj11dzy1Q/132\",\"privilege\":[],\"unionid\":\"oiPIJuEo0OzxLqzSEWZYZ-nVWmT3\"}");
	}
	
}


