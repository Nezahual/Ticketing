package my.aiorchestrator.infrastructure.adapters;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.outgoing.ChromaService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChromaServiceAdapter implements ChromaService {

    @PostConstruct
    public void init() {}
}
