package edu.albert.studycards.authserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.albert.studycards.authserver.domain.dto.UserAccountDto;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SourceProvider {
    private static final DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Random random = new Random(12441252L);

    private static final String JSON_DIR = "json/";
    private static final String LANG_PACK_DIR = "LangPack/";
    private static final String ACCOUNT_DIR = "Client/";
    private static final String DTO_DIR = "Dto/";
    private static final String PERSISTENT_DIR = "Persistent/";
    private static final String CLIENT_FILE_NAME = "Client";

    private static final int LANG_PACK_COUNT = 4;

//    static List<Account> getAccounts() throws IOException {
//        List<Account> accounts = new ArrayList<>();
//        accounts.addAll(getAccountDto(1));
//        accounts.addAll(getAccountPersistent());
//        return accounts;
//    }
//
//    static List<LangPackDto> getLangPackDto() throws IOException {
//        List<Resource> resources = loadResources(JSON_DIR + LANG_PACK_DIR + DTO_DIR, "LangPackDto", LANG_PACK_COUNT);
//        List<LangPackDto> packs = new ArrayList<>();
//
//        for (Resource res : resources) {
//            LangPackDtoImpl langPackDto = OBJECT_MAPPER.readValue(res.getFile(), LangPackDtoImpl.class);
//            packs.add(langPackDto);
//        }
//
//        return packs;
//    }
//
//    static List getLangPackPersistent() throws IOException {
//        List<Resource> resources = loadResources(JSON_DIR + LANG_PACK_DIR + PERSISTENT_DIR, "LangPackDto", LANG_PACK_COUNT);
//        List<LangPackDto> packs = new ArrayList<>();
//
//        for (Resource res : resources) {
//            LangPackDtoImpl langPackDto = OBJECT_MAPPER.readValue(res.getFile(), LangPackDtoImpl.class);
//            packs.add(langPackDto);
//        }
//
//        return packs;
//    }
//
//    private static List<CardDto> getCards() throws IOException {
//        List<LangPackDto> langPacks = getLangPackDto();
//        return List.of((CardDto) langPacks.get(0).getCard(0), (CardDto) langPacks.get(2).getCard(0));
//    }

    public static List<UserAccountDto> getAccountDto(int amount) throws IOException {
        List<Resource> resources = loadNResources(JSON_DIR + ACCOUNT_DIR + DTO_DIR, CLIENT_FILE_NAME, amount);
        List<UserAccountDto> packs = new ArrayList<>();

        for (Resource res : resources) {
            UserAccountDto accountDto = OBJECT_MAPPER.readValue(res.getFile(), UserAccountDto.class);
            packs.add(accountDto);
        }

        return packs;
    }

    public static List<UserAccountDto> getRandomAccountDto(int amount) {
        List<Resource> resources = loadNResources(JSON_DIR + ACCOUNT_DIR + DTO_DIR, CLIENT_FILE_NAME, amount);
        List<UserAccountDto> packs = new ArrayList<>();

        for (Resource res : resources) {
            UserAccountDto accountDto = null;
            try {
                accountDto = OBJECT_MAPPER.readValue(res.getFile(), UserAccountDto.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            accountDto = new UserAccountDto(
                    accountDto.firstName() + random.nextInt(),
                    accountDto.lastName() + random.nextInt(),
                    accountDto.email() + random.nextInt(),
                    accountDto.password(),
                    accountDto.role(),
                    accountDto.status());
            packs.add(accountDto);
        }

        return packs;
    }

    private static List<Resource> loadResources(final String resLocation, String resName, int amount) {
        List<Resource> resources = new ArrayList<>(amount);
        for (int i = 1; i <= amount; i++) {
            Resource resource = resourceLoader.getResource(resLocation + resName + i + ".json");
            resources.add(resource);
        }
        return resources;
    }

    private static List<Resource> loadNResources(final String resLocation, String resName, int n) {
        List<Resource> resources = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            Resource resource = resourceLoader.getResource(resLocation + resName + 1 + ".json");
            resources.add(resource);
        }
        return resources;
    }
}
