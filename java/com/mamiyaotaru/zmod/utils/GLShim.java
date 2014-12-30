package com.mamiyaotaru.zmod.utils;

import java.nio.FloatBuffer;

import net.minecraft.client.renderer.GlStateManager;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GLShim {
	
	//GL11
	public static final int GL_ALPHA_TEST = GL11.GL_ALPHA_TEST;
	public static final int GL_BLEND = GL11.GL_BLEND;
	public static final int GL_CLAMP = GL11.GL_CLAMP;
	public static final int GL_COLOR_BUFFER_BIT = GL11.GL_COLOR_BUFFER_BIT;
	public static final int GL_COLOR_CLEAR_VALUE = GL11.GL_COLOR_CLEAR_VALUE;
	public static final int GL_CULL_FACE = GL11.GL_CULL_FACE;
	public static final int GL_DEPTH_BUFFER_BIT = GL11.GL_DEPTH_BUFFER_BIT;
	public static final int GL_DST_ALPHA = GL11.GL_DST_ALPHA;
	public static final int GL_DST_COLOR = GL11.GL_DST_COLOR;
	public static final int GL_FOG = GL11.GL_FOG;
	public static final int GL_DEPTH_TEST = GL11.GL_DEPTH_TEST;
	public static final int GL_FLAT = GL11.GL_FLAT;
	public static final int GL_FOG_DENSITY = GL11.GL_FOG_DENSITY;
	public static final int GL_FOG_END = GL11.GL_FOG_END;
	public static final int GL_FOG_MODE = GL11.GL_FOG_MODE;
	public static final int GL_FOG_START = GL11.GL_FOG_START;
	public static final int GL_PROJECTION_MATRIX = GL11.GL_PROJECTION_MATRIX;
	public static final int GL_GREATER = GL11.GL_GREATER;
	public static final int GL_LIGHTING = GL11.GL_LIGHTING;
	public static final int GL_LINEAR = GL11.GL_LINEAR;
	public static final int GL_LINES = GL11.GL_LINES;
	public static final int GL_MODELVIEW = GL11.GL_MODELVIEW;
	public static final int GL_NEAREST = GL11.GL_NEAREST;
	public static final int GL_NORMALIZE = GL11.GL_NORMALIZE;
	public static final int GL_ONE = GL11.GL_ONE;
	public static final int GL_ONE_MINUS_DST_ALPHA = GL11.GL_ONE_MINUS_DST_ALPHA;
	public static final int GL_ONE_MINUS_SRC_ALPHA = GL11.GL_ONE_MINUS_SRC_ALPHA;
	public static final int GL_POLYGON_OFFSET_FILL = GL11.GL_POLYGON_OFFSET_FILL;
	public static final int GL_PROJECTION = GL11.GL_PROJECTION;
	public static final int GL_QUADS = GL11.GL_QUADS;
	public static final int GL_SMOOTH = GL11.GL_SMOOTH;
	public static final int GL_SRC_ALPHA = GL11.GL_SRC_ALPHA;
	public static final int GL_TEXTURE_2D = GL11.GL_TEXTURE_2D;
	public static final int GL_TEXTURE_HEIGHT = GL11.GL_TEXTURE_HEIGHT;
	public static final int GL_TEXTURE_MAG_FILTER = GL11.GL_TEXTURE_MAG_FILTER;
	public static final int GL_TEXTURE_MIN_FILTER = GL11.GL_TEXTURE_MIN_FILTER;
	public static final int GL_TEXTURE_WIDTH = GL11.GL_TEXTURE_WIDTH;
	public static final int GL_TEXTURE_WRAP_S = GL11.GL_TEXTURE_WRAP_S;
	public static final int GL_TEXTURE_WRAP_T = GL11.GL_TEXTURE_WRAP_T;
	public static final int GL_TRANSFORM_BIT = GL11.GL_TRANSFORM_BIT;
	public static final int GL_VIEWPORT_BIT = GL11.GL_VIEWPORT_BIT;
	public static final int GL_ZERO = GL11.GL_ZERO;
	
	//GL12
	public static final int GL_RESCALE_NORMAL = GL12.GL_RESCALE_NORMAL;


	public static void glEnable(int attrib) {
		switch (attrib) {
			case GL_ALPHA_TEST:
				GlStateManager.enableAlpha();
				break;
			case GL_BLEND:
				GlStateManager.enableBlend();
				break;
			case GL_CULL_FACE:
				GlStateManager.enableCull();
				break;
			case GL_DEPTH_TEST:
				GlStateManager.enableDepth();
				break;
			case GL_FOG:
				GlStateManager.enableFog();
				break;
			case GL_LIGHTING:
				GlStateManager.enableLighting();
				break;
			case GL_NORMALIZE:
				GlStateManager.enableNormalize();
				break;
			case GL_POLYGON_OFFSET_FILL:
				GlStateManager.enablePolygonOffset();
				break;
			case GL_RESCALE_NORMAL:
				GlStateManager.enableRescaleNormal();
				break;
			case GL_TEXTURE_2D:
				GlStateManager.enableTexture2D();
				break;
		}
	}

	public static void glDisable(int attrib) {
		switch (attrib) {
			case GL_ALPHA_TEST:
				GlStateManager.disableAlpha();
				break;
			case GL_BLEND:
				GlStateManager.disableBlend();
				break;
			case GL_CULL_FACE:
				GlStateManager.disableCull();
				break;
			case GL_DEPTH_TEST:
				GlStateManager.disableDepth();
				break;
			case GL_FOG:
				GlStateManager.disableFog();
				break;
			case GL_LIGHTING:
				GlStateManager.disableLighting();
				break;
			case GL_NORMALIZE:
				GlStateManager.disableNormalize();
				break;
			case GL_POLYGON_OFFSET_FILL:
				GlStateManager.disablePolygonOffset();
				break;
			case GL_RESCALE_NORMAL:
				GlStateManager.disableRescaleNormal();
				break;
			case GL_TEXTURE_2D:
				GlStateManager.disableTexture2D();
				break;
		}
	}

	public static void glFogi(int pname, int param) {
		switch (pname) {
			case GL_FOG_MODE:
				GlStateManager.setFog(param);
				break;
		}
	}

	public static void glFogf(int pname, float param) {
		switch (pname) {
			case GL_FOG_DENSITY:
				GlStateManager.setFogDensity(param);
				break;
			case GL_FOG_END:
				GlStateManager.setFogEnd(param);
				break;
			case GL_FOG_START:
				GlStateManager.setFogStart(param);
				break;
		}
	}

	public static void glAlphaFunc(int func, float ref) {
		GlStateManager.alphaFunc(func, ref);
	}
	
	public static void glBlendFunc(int sfactor, int dfactor) {
		GlStateManager.blendFunc(sfactor, dfactor);
	}

	public static void glBlendFuncSeparate(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha) {
		GlStateManager.tryBlendFuncSeparate(sfactorRGB, dfactorRGB,	sfactorAlpha, dfactorAlpha);
	}
	
	public static void glCallList(int list) {
		GlStateManager.callList(list);
	}
	
	public static void glClear(int mask) {
		GlStateManager.clear(mask);
	}
	
	public static void glClearColor(float red, float green, float blue,
			float alpha) {
		GlStateManager.clearColor(red, green, blue, alpha);
	}
	
	public static void glClearDepth(double depth) {
		GlStateManager.clearDepth(depth);
	}
	
	public static void glColor3f(float red, float green, float blue) {
		GlStateManager.color(red, green, blue, 1.0F);
	}
	
	public static void glColor3ub(int red, int green, int blue) {
		GlStateManager.color((float)red/255f, (float)green/255f, (float)blue/255f, 1.0F);
	}
	
	public static void glColor4f(float red, float green, float blue, float alpha) {
		GlStateManager.color(red, green, blue, alpha);
	}

	public static void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		GlStateManager.colorMask(red, green, blue, alpha);
	}

	public static void glColorMaterial(int face, int mode) {
		GlStateManager.colorMaterial(face, mode);
	}
	
	public static void glCullFace(int mode) {
		GlStateManager.cullFace(mode);
	}

	public static void glDepthFunc(int func) {
		GlStateManager.depthFunc(func);
	}

	public static void glDepthMask(boolean flag) {
		GlStateManager.depthMask(flag);
	}
	
	public static void glGetFloat(int pname, FloatBuffer params) {
		GlStateManager.getFloat(pname, params);
	}
	
	public static void glLoadIdentity() {
		GlStateManager.loadIdentity();
	}
	
	public static void glLogicOp(int opcode) {
		GlStateManager.colorLogicOp(opcode);
	}
	
	public static void glMatrixMode(int mode) {
		GlStateManager.matrixMode(mode);
	}
	
	public static void glMultMatrix(FloatBuffer m) {
		GlStateManager.multMatrix(m);
	}
	
	public static void glOrtho(double left, double right, double bottom, double top, double zNear, double zFar) {
		GlStateManager.ortho(left, right, bottom, top, zNear, zFar);
	}

	public static void glPolygonOffset(float factor, float units) {
		GlStateManager.doPolygonOffset(factor, units);
	}
	
	public static void glPopAttrib() {
		GlStateManager.popAttrib();
	}
	
	public static void glPopMatrix() {
		GlStateManager.popMatrix();
	}
	
	public static void glPushAttrib() {
		GlStateManager.pushAttrib();
	}

	public static void glPushMatrix() {
		GlStateManager.pushMatrix();
	}
	
	public static void glRotatef(float angle, float x, float y, float z) {
		GlStateManager.rotate(angle, x, y, z);
	}
	
	public static void glScaled(double x, double y, double z) {
		GlStateManager.scale(x, y, z);
	}

	public static void glScalef(float x, float y, float z) {
		GlStateManager.scale(x, y, z);
	}

	public static void glSetActiveTextureUnit(int texture) {
		GlStateManager.setActiveTexture(texture);
	}

	public static void glShadeModel(int mode) {
		GlStateManager.shadeModel(mode);
	}
	
	public static void glTranslated(double x, double y, double z) {
		GlStateManager.translate(x, y, z);
	}

	public static void glTranslatef(float x, float y, float z) {
		GlStateManager.translate(x, y, z);
	}

	public static void glViewport(int x, int y, int width, int height) {
		GlStateManager.viewport(x, y, width, height);
	}

	public static void glBegin(int mode) {
		GL11.glBegin(mode);
	}
	
	public static void glBindTexture(int target, int texture) {
		switch (target) {
		case GL_TEXTURE_2D:
			GlStateManager.bindTexture(texture);
			break;
		default:
			GL11.glBindTexture(target, texture);			
		}
		// I only seem to call this with GL_TEXTURE_2D as target, which is what bindTexture in GlStateManager does
		// decide if I even need to specify the target in those instances.  If not, could dump this method
	}

	public static void glEnd() {
		GL11.glEnd();
	}
	
	public static boolean glGetBoolean(int pname) {
		return GL11.glGetBoolean(pname);
	}
	
	public static int glGetTexLevelParameteri(int target, int level, int pname) {
		return GL11.glGetTexLevelParameteri(target, level, pname);
	}
	
	public static void glLoadMatrix(FloatBuffer buf) {
		GL11.glLoadMatrix(buf);
	}
	
	public static void glNormal3f(float nx, float ny, float nz) {
		GL11.glNormal3f(nx, ny, nz);
	}
	
	public static void glPushAttrib(int mask) {
		GL11.glPushAttrib(mask);
	}

	public static void glTexParameteri(int target, int pname, int param) {
		GL11.glTexParameteri(target, pname, param);
	}
	
	public static void glVertex2f(float x, float y) {
		GL11.glVertex2f(x, y);
	}
	
	public static void glVertex3f(float x, float y, float z) {
		GL11.glVertex3f(x, y, z);
	}
}
