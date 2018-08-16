package com.gilmarcarlos.developer.gcursos.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class PushControler {
	
	public static final Map<Long, SseEmitter> emitters = Collections.synchronizedMap( new HashMap<>());
	
	@GetMapping(value = "/notificacoes/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseBody
	public SseEmitter quotes(@PathVariable("id") Long id) {
		SseEmitter emitter = new SseEmitter(180_000L);
	   	emitters.put(id, emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
	    return emitter;
	}
	
public static void notificacoes(Long id, String msg) {
		
		Map<Long, SseEmitter> sseEmitterListToRemove = new HashMap<>();
            try {
            	emitters.get(id).send(msg);
            } catch (IOException e) {
            	emitters.get(id).complete();
                sseEmitterListToRemove.put(id, emitters.get(id));
                e.printStackTrace();
            }
            
        emitters.remove(id);
	}
	
}
	
	
