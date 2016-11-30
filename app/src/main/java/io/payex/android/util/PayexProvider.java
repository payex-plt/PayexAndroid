package io.payex.android.util;

import android.nfc.tech.IsoDep;
import android.util.Log;

import com.github.devnied.emvnfccard.enums.SwEnum;
import com.github.devnied.emvnfccard.exception.CommunicationException;
import com.github.devnied.emvnfccard.parser.IProvider;
import com.github.devnied.emvnfccard.utils.TlvUtil;

import java.io.IOException;

/**
 * Created by vince on 11/30/2016.
 */

public class PayexProvider implements IProvider {

    private IsoDep mTagCom;

    @Override
    public byte[] transceive(byte[] pCommand) throws CommunicationException {
        byte[] response = null;
        try {
            // send command to emv card
            response = mTagCom.transceive(pCommand);
        } catch (IOException e) {
            throw new CommunicationException(e.getMessage());
        }

        try {
            SwEnum val = SwEnum.getSW(response);
        } catch (Exception e) {
        }

        return response;
    }

    public void setmTagCom(final IsoDep mTagCom) {
        this.mTagCom = mTagCom;
    }

}
