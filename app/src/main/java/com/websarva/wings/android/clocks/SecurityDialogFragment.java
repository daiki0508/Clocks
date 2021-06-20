package com.websarva.wings.android.clocks;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SecurityDialogFragment {
    public static class RootCheckDialog extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstance){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("RootDetected");
            builder.setMessage("Rootを検知しました");
            builder.setPositiveButton("OK", (dialogInterface, i) -> requireActivity().finish());
            this.setCancelable(false);

            return builder.create();
        }
    }

    public static class DebugCheckDialog extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstance){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("isDebuggable");
            builder.setMessage("デバッグモードで起動しています！");
            builder.setPositiveButton("OK", (dialogInterface, i) -> requireActivity().finish());
            this.setCancelable(false);

            return builder.create();
        }
    }
}
