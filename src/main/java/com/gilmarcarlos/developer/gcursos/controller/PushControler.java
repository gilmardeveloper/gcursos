package com.gilmarcarlos.developer.gcursos.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class PushControler {
	
	public static final List<SseEmitter> emitters = Collections.synchronizedList( new ArrayList<>());
	
	@GetMapping(value = "/notificacoes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseBody
	public SseEmitter quotes() {
		SseEmitter emitter = new SseEmitter(180_000L);
	   	emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
				
	    return emitter;
	}
	
public static void notificacoes(String msg) {
		
		List<SseEmitter> sseEmitterListToRemove = new ArrayList<>();
        emitters.forEach((SseEmitter emitter) -> {
            try {
                emitter.send(msg);
            } catch (IOException e) {
                emitter.complete();
                sseEmitterListToRemove.add(emitter);
                e.printStackTrace();
            }
        });
        emitters.removeAll(sseEmitterListToRemove);
	}
	
}
	
	
