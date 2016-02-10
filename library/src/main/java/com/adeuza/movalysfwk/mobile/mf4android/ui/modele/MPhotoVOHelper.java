package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import java.sql.Timestamp;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhotoState;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MPhotoVO;

public final class MPhotoVOHelper {

	private MPhotoVOHelper(){

	}
	/**
	 * @param oPmd
	 */
	public static boolean updateToPhotoVO(AndroidPhotoMetaData p_oPmd, MPhotoVO p_oPhotoVO) {
		boolean bDm = false;

		p_oPhotoVO.setId_id(p_oPmd.getId());
		

		boolean bLocalUriModified = (p_oPhotoVO.getLocalUri()== null && p_oPmd.getLocalUriAsString()!=null)
				|| (p_oPhotoVO.getLocalUri() != null && !p_oPhotoVO.getLocalUri().equals(p_oPmd.getLocalUriAsString()));
		boolean bDateModified = (p_oPhotoVO.getDate()==null && p_oPmd.getDateTaken()!=0) 
				|| (p_oPhotoVO.getDate() != null && p_oPhotoVO.getDate().getTime() != p_oPmd.getDateTaken() );		

		if ( bLocalUriModified || bDateModified ) {
			bDm = true;
		}

		p_oPhotoVO.setName(p_oPmd.getName());
		p_oPhotoVO.setDescription(p_oPmd.getDescription());
		if (p_oPmd.isPhotoDownloaded()) {
			p_oPhotoVO.setPhotoState(MPhotoState.DOWNLOADED);
		}
		else if (p_oPmd.isPhotoSelected()) {
			p_oPhotoVO.setPhotoState(MPhotoState.SELECTED);
		}
		else if (p_oPmd.isPhotoShot()) {
			p_oPhotoVO.setPhotoState(MPhotoState.TAKEN);
		}
		p_oPhotoVO.setUri(p_oPmd.getLocalUriAsString());
		p_oPhotoVO.setDate(new Timestamp(p_oPmd.getDateTaken()));
		//this.refreshPhotoThumbnailVO();
		return bDm;
	}

	/**
	 * @param p_oPhotoVM
	 * @param p_oPhotoMetaData
	 */
	public static void updateToPhotoMetaData(MPhotoVO p_oPhotoVM, AndroidPhotoMetaData p_oPhotoMetaData) {
		if (p_oPhotoVM != null) {
			if ( p_oPhotoVM.getDate() != null ) {
				p_oPhotoMetaData.setDateTaken(p_oPhotoVM.getDate().getTime());
			}
			p_oPhotoMetaData.setDescription(p_oPhotoVM.getDescription());
			p_oPhotoMetaData.setLocalUri(p_oPhotoVM.getLocalUri());
			p_oPhotoMetaData.setRemoteUri(p_oPhotoVM.getRemoteUri());
			p_oPhotoMetaData.setName(p_oPhotoVM.getName());
			if ( MPhotoState.DOWNLOADED.equals(p_oPhotoVM.getPhotoState())) {
				p_oPhotoMetaData.setPhotoAsDownloaded();
			}
			if ( MPhotoState.SELECTED.equals(p_oPhotoVM.getPhotoState())) {
				p_oPhotoMetaData.setPhotoAsSelected();
			}
			if ( MPhotoState.TAKEN.equals(p_oPhotoVM.getPhotoState())) {
				p_oPhotoMetaData.setPhotoAsShooted();
			}
			//p_oPhotoMetaData.setPrivate(p_bPrivate);
		}
	}
}
