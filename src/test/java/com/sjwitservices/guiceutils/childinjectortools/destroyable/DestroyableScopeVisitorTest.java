package com.sjwitservices.guiceutils.childinjectortools.destroyable;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.lang.annotation.Annotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Scope;

@RunWith(MockitoJUnitRunner.class)
public class DestroyableScopeVisitorTest {

	@Mock
	DestroyableScope correctScope;
	@Mock Scope differentScope; 
	
	DestroyableScopeVisitor visitor = new DestroyableScopeVisitor();
	
	@Test
	public void visit_eager_singleton_returns_false() {
		assertFalse(visitor.visitEagerSingleton());
	}
	
	@Test
	public void visit_no_scoping_returns_false() {
		assertFalse(visitor.visitNoScoping());
	}
	
	@Test
	public void visit_scope_with_destroyable_scope_returns_true() {
		assertTrue(visitor.visitScope(correctScope));
	}
	
	@Test
	public void visit_scope_with_different_scope_returns_false() {
		assertFalse(visitor.visitScope(differentScope));
	}
	
	@Test
	public void visit_scope_annotation_with_DestroyableComponentSingleton_annotation_returns_true() {
		assertTrue(visitor.visitScopeAnnotation(DestroyableComponentSingleton.class));
	}
	
	@Test
	public void visit_scope_annotation_with_different_annotation_returns_false() {
		assertFalse(visitor.visitScopeAnnotation(DifferentAnnotation.class));
	}
	
	private interface DifferentAnnotation extends Annotation {}
}
