package project.bayaraja.application.seeder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import project.bayaraja.application.services.lookups.LookupEntity;
import project.bayaraja.application.services.lookups.interfaces.LookupRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration @Transactional @RequiredArgsConstructor
public class LookupSeeder implements ApplicationRunner {

    private final LookupRepository lookupRepository;

    private static final Logger log = LoggerFactory.getLogger(LookupSeeder.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(args.getOptionValues("seeder") != null){
            List<String> seeder = Arrays.asList(args.getOptionValues("seeder").get(0).split(","));
            if(seeder.contains("lookup")){
                seedMajor();
                log.info("Seeding success [LOOKUP - MAJOR]");
            }
        } else {
            log.info("Seed lookup skipped");
        }
    }

    private void seedMajor(){
        List<List<String>> lookups = new ArrayList<>();

        //type[0] key[1] value[2]
        lookups.add(List.of("MAJOR_CLASS", "TJKT", "Teknik Komputer dan Jaringan"));
        lookups.add(List.of("MAJOR_CLASS", "TO", "Teknik Otomotif"));
        lookups.add(List.of("MAJOR_CLASS", "TE", "Teknik Elektro"));
        lookups.add(List.of("MAJOR_CLASS", "AK", "Akuntansi Keuangan"));

        for(var lookup : lookups){
            this.lookupRepository.save(
                    LookupEntity.builder()
                            .type(lookup.get(0))
                            .key(lookup.get(1))
                            .value(lookup.get(2))
                            .build()
            );
        }
    }
}
