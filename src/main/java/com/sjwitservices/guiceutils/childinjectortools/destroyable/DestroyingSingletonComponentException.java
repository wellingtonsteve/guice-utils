package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
public class DestroyingSingletonComponentException extends RuntimeException {
	@Getter private final List<Exception> thrownExceptions;
}