package com.pspdfkit.flutter.pspdfkit;

import android.content.Context;
import android.net.Uri;

import com.pspdfkit.PSPDFKit;
import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.ui.PdfActivity;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * Pspdfkit Plugin.
 */
public class PspdfkitPlugin implements MethodCallHandler {
    private static final String FILE_SCHEME = "file:///";
    private final Context context;

    public PspdfkitPlugin(Context context) {
        this.context = context;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "pspdfkit");
        channel.setMethodCallHandler(new PspdfkitPlugin(registrar.activeContext()));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        switch (call.method) {
            case "frameworkVersion":
                result.success("Android " + PSPDFKit.VERSION);
                break;
            case "present":
                String documentPath = call.argument("document");
                if (documentPath == null) {
                    throw new IllegalArgumentException("Document path may not be null.");
                }
                if (documentPath.isEmpty()) {
                    throw new IllegalArgumentException("Document path may not be empty.");
                }
                if (Uri.parse(documentPath).getScheme() == null) {
                    if (documentPath.startsWith("/")) {
                        documentPath = documentPath.substring(1);
                    }
                    documentPath = FILE_SCHEME + documentPath;
                }
                PdfActivity.showDocument(context, Uri.parse(documentPath), new PdfActivityConfiguration.Builder(context).build());
                break;
            default:
                result.notImplemented();
                break;
        }
    }
}
